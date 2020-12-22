package ca.mcgill.ecse321.smartart.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.smartart.dao.BuyerRepository;
import ca.mcgill.ecse321.smartart.dao.PostingRepository;
import ca.mcgill.ecse321.smartart.dao.PurchaseRepository;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.PurchaseDto;
import ca.mcgill.ecse321.smartart.model.ArtStatus;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.DeliveryType;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.model.Purchase;

@Service
public class PurchaseService {
  @Autowired private BuyerRepository buyerRepository;
  @Autowired private PurchaseRepository purchaseRepository;
  @Autowired private PostingRepository postingRepository;
  @Autowired private ServiceHelper helper;

  /**
   * Creates a Purchase with a purchaseID and a buyer.
   *
   * @param purchaseID: the ID of the Purchase.
   * @param buyer: the buyer of the Purchase.
   * @return the created Purchase.
   */
  @Transactional
  public Purchase createPurchase(int purchaseID, Buyer buyer) {
    // Input validation
    String error = "";
    if (buyer == null) {
      error = error + "Purchase buyer cannot be empty. ";
    }
    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }
    if (purchaseRepository.findPurchaseByPurchaseID(purchaseID) != null) {
      throw new IllegalArgumentException("A purchase with this ID already exists.");
    }

    Purchase purchase = new Purchase();
    purchase.setPurchaseID(purchaseID);
    purchase.setBuyer(buyer);
    purchase.setTotalPrice(0);
    buyer.addPurchase(purchase);
    purchaseRepository.save(purchase);
    buyerRepository.save(buyer);
    return purchase;
  }

  /**
   * Creates a Purchase using Dto data.
   *
   * @param data: the data from PurchaseDto.
   * @return the created Purchase.
   * @throws IllegalArgumentException
   */
  @Transactional
  public Purchase createPurchase(PurchaseDto data) throws IllegalArgumentException {
    int purchaseID = generatePurchaseID(helper.convertToModel(data.getBuyer()));
    Purchase purchase = createPurchase(purchaseID, helper.convertToModel(data.getBuyer()));
    return purchase;
  }

  /**
   * Gets a Purchase by its purchaseID.
   *
   * @param purchaseID: the ID of the Purchase.
   * @return the retrieved Purchase.
   */
  @Transactional
  public Purchase getPurchase(int purchaseID) {
    Purchase purchase = purchaseRepository.findPurchaseByPurchaseID(purchaseID);
    return purchase;
  }

  /**
   * Gets a list of all Purchases of a Buyer from their email.
   *
   * @param email: the Buyer's email.
   * @return the Buyer's list of Purchases.
   */
  @Transactional
  public List<Purchase> getPurchasesByBuyer(String email) {
    Buyer buyer = buyerRepository.findBuyerByEmail(email);
    if (buyer == null) throw new IllegalArgumentException("No buyer with email " + email);
    return helper.toList(buyer.getPurchases());
  }

  /**
   * Gets a Buyer's cart using their email.
   *
   * @param email: the Buyer's email.
   * @return the Buyer's cart.
   */
  @Transactional
  public Purchase getCart(String email) {
    Buyer buyer = buyerRepository.findBuyerByEmail(email);

    if (buyer == null) throw new IllegalArgumentException("No buyer with email " + email);

    return buyer.getCart();
  }

  /**
   * Gets the list of all Purchases in the purchase repository.
   *
   * @return the list of all Purchases.
   */
  @Transactional
  public List<Purchase> getAllPurchases() {
    return toList(purchaseRepository.findAll());
  }

  /**
   * Makes a Purchase (the Buyer's cart) with a delivery type.
   *
   * @param cart: the Purchase in the cart.
   * @param deliveryType: the type of Delivery for the Purchase.
   * @return the Purchase made.
   */
  @Transactional
  public Purchase makePurchase(Purchase cart, DeliveryType deliveryType) {
    if (cart == null)
      throw new IllegalArgumentException("Must have a purchase order to make purchase");

    if (deliveryType != DeliveryType.PickUp && deliveryType != DeliveryType.Shipped)
      throw new IllegalArgumentException("Delivery Type not valid");

    Buyer buyer = cart.getBuyer();

    for (Posting p : cart.getPostings()) {
      p.setArtStatus(ArtStatus.Purchased);
    }

    cart.setDeliveryType(deliveryType);
    cart.setTotalPrice(calcFinalPrice(cart));
    LocalDateTime now = LocalDateTime.now();
    cart.setTime(now);
    buyer.setCart(null);

    buyerRepository.save(buyer);
    purchaseRepository.save(cart);

    return cart;
  }

  /**
   * Makes a Purchase with a delivery type using Dto data.
   *
   * @param data: the data from PurchaseDto.
   * @param deliveryType: the type of delivery for the Purchase.
   * @return the Purchase made.
   * @throws IllegalArgumentException
   */
  @Transactional
  public Purchase makePurchase(PurchaseDto data, DeliveryType deliveryType)
      throws IllegalArgumentException {
    Purchase purchase = purchaseRepository.findPurchaseByPurchaseID(data.getPurchaseID());
    System.out.println("PurchaseID: " + data.getPurchaseID());
    return makePurchase(purchase, deliveryType);
  }

  /**
   * Cancels a Purchase.
   *
   * @param purchase: the purchase to be cancelled.
   * @return the cancelled Purchase.
   */
  @Transactional
  public boolean cancelPurchase(Purchase purchase) {
    if (purchase == null)
      throw new IllegalArgumentException("Must have a purchase to cancel purchase");

    LocalDateTime now = LocalDateTime.now();
    Buyer buyer = purchase.getBuyer();

    if (now.minusMinutes(10).isAfter(purchase.getTime())) return false;

    for (Posting p : purchase.getPostings()) p.setArtStatus(ArtStatus.Available);

    buyer.removePurchase(purchase);
    buyerRepository.save(buyer);
    purchaseRepository.delete(purchase);
    return true;
  }

  /**
   * Cancels a Purchase using Dto data.
   *
   * @param data: the data from PurchaseDto.
   * @return the cancelled Purchase.
   * @throws IllegalArgumentException
   */
  @Transactional
  public boolean cancelPurchase(PurchaseDto data) throws IllegalArgumentException {
    Purchase purchase = purchaseRepository.findPurchaseByPurchaseID(data.getPurchaseID());
    if (purchase == null) {
      throw new IllegalArgumentException("Purchase does not exist");
    }
    return cancelPurchase(purchase);
  }

  /**
   * Adds a Posting to a Buyer's cart.
   *
   * @param buyer: the Buyer of the Posting.
   * @param posting: the Posting to be added in the cart.
   * @return the Purchase (cart) of the Buyer.
   */
  @Transactional
  public Purchase addToCart(Buyer buyer, Posting posting) {
    // Input validation
    String error = "";
    if (buyer == null) {
      error = error + "addToCart buyer cannot be empty. ";
    }
    if (posting == null) {
      error = error + "addToCart posting cannot be empty. ";
      throw new IllegalArgumentException(error);
    }
    if (posting.getArtStatus() != ArtStatus.Available) {
      error = error + "addToCart posting cannot be On Hold or Purchased. ";
    }
    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }

    Purchase cart = buyer.getCart();
    if (cart == null) {
      int id = generatePurchaseID(buyer);
      cart = createPurchase(id, buyer);
      buyer.setCart(cart);
    }
    cart.addPosting(posting);
    posting.setArtStatus(ArtStatus.OnHold);
    purchaseRepository.save(cart);
    postingRepository.save(posting);
    buyerRepository.save(buyer);
    return cart;
  }

  /**
   * Adds a Posting to the Buyer's cart using Dto data.
   *
   * @param buyerData: the data from BuyerDto.
   * @param postingID: the ID of the posting.
   * @return the Purchase to be added.
   * @throws IllegalArgumentException
   */
  @Transactional
  public Purchase addToCart(BuyerDto buyerData, int postingID) throws IllegalArgumentException {
    Buyer buyer = buyerRepository.findBuyerByEmail(buyerData.getEmail());
    Posting posting = postingRepository.findPostingByPostingID(postingID);
    if (buyer == null) {
      throw new IllegalArgumentException("Buyer does not exist.");
    }
    if (posting == null) {
      throw new IllegalArgumentException("Posting does not exist.");
    }

    return addToCart(buyer, posting);
  }
  
  @Transactional
  public Purchase addToCart(String email, int postingID) throws IllegalArgumentException {
    Buyer buyer = buyerRepository.findBuyerByEmail(email);
    Posting posting = postingRepository.findPostingByPostingID(postingID);
    if (buyer == null) {
      throw new IllegalArgumentException("Buyer does not exist.");
    }
    if (posting == null) {
      throw new IllegalArgumentException("Posting does not exist.");
    }

    return addToCart(buyer, posting);
  }

  /**
   * Removes a Posting from the Buyer's cart.
   *
   * @param buyer: the Buyer of the Purchase.
   * @param posting: the Posting to be bought.
   * @return the cart of the Buyer.
   */
  @Transactional
  public Purchase removeFromCart(Buyer buyer, Posting posting) {
    // Input validation
    String error = "";
    if (buyer == null) {
      error = error + "removeFromCart buyer cannot be empty. ";
      throw new IllegalArgumentException(error);
    }

    Purchase cart = buyer.getCart();
    if (cart == null) {
      error = error + "removeFromCart cart cannot be null. ";
    }
    if (posting == null) {
      error = error + "removeFromCart posting cannot be empty. ";
    }
    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }

    if (cart.removePosting(posting)) {
      posting.setArtStatus(ArtStatus.Available);
    }
    purchaseRepository.save(cart);
    postingRepository.save(posting);
    return cart;
  }

  /**
   * Removes a Posting from the cart using Dto data.
   *
   * @param buyerData: the data from BuyerDto.
   * @param postingID: the ID of the Posting.
   * @return the cart of the Buyer.
   * @throws IllegalArgumentException
   */
  @Transactional
  public Purchase removeFromCart(BuyerDto buyerData, int postingID)
      throws IllegalArgumentException {
    Buyer buyer = buyerRepository.findBuyerByEmail(buyerData.getEmail());
    Posting posting = postingRepository.findPostingByPostingID(postingID);
    if (buyer == null) {
      throw new IllegalArgumentException("Invalid buyer");
    }
    if (posting == null) {
      throw new IllegalArgumentException("Invalid posting");
    }
    return removeFromCart(buyer, posting);
  }

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

  /**
   * Generates the ID of a purchase.
   *
   * @param buyer: the Buyer of the purchase.
   * @return the ID of a purchase.
   */
  private int generatePurchaseID(Buyer buyer) {
    if (buyer == null) throw new IllegalArgumentException("Purchase buyer cannot be empty");
    int purchaseID = Math.abs(buyer.getEmail().hashCode());
    Random r = new Random();
    while (purchaseRepository.findPurchaseByPurchaseID(purchaseID) != null) {
      purchaseID = Math.abs(purchaseID + r.nextInt());
    }
    return purchaseID;
  }

  /**
   * Calculates the final price of a Purchase.
   *
   * @param purchase: the Purchase of the Buyer.
   * @return the final price of the Purchase.
   */
  private int calcFinalPrice(Purchase purchase) {
    Gallery gallery = purchase.getBuyer().getGallery();
    return (int) (purchase.getTotalPrice() * (1 + gallery.getCommission()));
  }
}
