package ca.mcgill.ecse321.smartart.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.smartart.dao.AdministratorRepository;
import ca.mcgill.ecse321.smartart.dao.ArtistRepository;
import ca.mcgill.ecse321.smartart.dao.BuyerRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dao.PostingRepository;
import ca.mcgill.ecse321.smartart.dao.PurchaseRepository;
import ca.mcgill.ecse321.smartart.dto.AdministratorDto;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.dto.PostingDto;
import ca.mcgill.ecse321.smartart.dto.PurchaseDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.ArtStatus;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.DeliveryType;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.model.Purchase;

@Service
public class ServiceHelper {

  @Autowired private ArtistRepository artistRepository;
  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private PostingRepository postingRepository;
  @Autowired private PurchaseRepository purchaseRepository;

  /** Clears all repositories of the Smart Art application. */
  @Transactional
  public void clearDatabase() {
    galleryRepository.deleteAll();
    artistRepository.deleteAll();
    buyerRepository.deleteAll();
    administratorRepository.deleteAll();
    purchaseRepository.deleteAll();
    postingRepository.deleteAll();
  }

  /**
   * Converts PurchaseDto data to the model.
   *
   * @param data: data from PurchaseDto.
   * @return the Purchase derived from PurchaseDto data.
   */
  Purchase convertToModel(PurchaseDto data) {
    if (data == null) return null;
    int purchaseID = data.getPurchaseID();
    Purchase purchase = purchaseRepository.findPurchaseByPurchaseID(purchaseID);

    if (purchase == null) {

      purchase = new Purchase();

      Buyer buyer = convertToModel(data.getBuyer());
      DeliveryType delivery = data.getDeliveryType();
      int totalPrice = data.getTotalPrice();
      LocalDateTime time = data.getTime();
      for (PostingDto p : data.getPostings()) {
        purchase.addPosting(convertToModel(p));
      }
      purchase.setBuyer(buyer);
      purchase.setDeliveryType(delivery);
      purchase.setPurchaseID(purchaseID);
      purchase.setTotalPrice(totalPrice);
      purchase.setTime(time);
    }

    return purchase;
  }

  /**
   * Converts AdministratorDto data to the model.
   *
   * @param data: data from AdministratorDto.
   * @return the Administrator derived from AdministratorDto data.
   */
  Administrator convertToModel(AdministratorDto data) {
    if (data == null) return null;
    String email = data.getEmail();
    Administrator admin = administratorRepository.findAdministratorByEmail(email);

    if (admin == null) {
      String name = data.getName();
      String password = data.getPassword();
      Gallery gallery = convertToModel(data.getGallery());

      admin = new Administrator();

      admin.setEmail(email);
      admin.setGallery(gallery);
      admin.setName(name);
      admin.setPassword(password);
    }

    return admin;
  }

  /**
   * Converts BuyerDto data to the model.
   *
   * @param data: data from BuyerDto.
   * @return the Buyer derived from BuyerDto data.
   */
  Buyer convertToModel(BuyerDto data) {
    if (data == null) return null;
    String email = data.getEmail();
    Buyer buyer = buyerRepository.findBuyerByEmail(email);

    if (buyer == null) {
      String name = data.getName();
      String password = data.getPassword();
      Gallery gallery = convertToModel(data.getGallery());

      buyer = new Buyer();

      buyer.setEmail(email);
      buyer.setGallery(gallery);
      buyer.setName(name);
      buyer.setPassword(password);
    }

    return buyer;
  }

  /**
   * Converts PostingDto data to the model.
   *
   * @param data: data from PostingDto.
   * @return the Posting derived from PostingDto data.
   */
  Posting convertToModel(PostingDto data) {
    if (data == null) return null;
    int postingID = data.getPostingID();
    Posting posting = postingRepository.findPostingByPostingID(postingID);

    if (posting == null) {

      posting = new Posting();

      Artist artist = convertToModel(data.getArtist());
      int price = data.getPrice();
      String title = data.getTitle();
      String description = data.getDescription();
      double xDim = data.getXDim();
      double yDim = data.getYDim();
      double zDim = data.getZDim();
      Date date = data.getDate();
      Gallery gallery = convertToModel(data.getGallery());
      ArtStatus status = data.getArtStatus();
      posting = new Posting();
      posting.setArtist(artist);
      posting.setPrice(price);
      posting.setTitle(title);
      posting.setDescription(description);
      posting.setXDim(xDim);
      posting.setYDim(yDim);
      posting.setZDim(zDim);
      posting.setDate(date);
      posting.setGallery(gallery);
      posting.setArtStatus(status);
    }

    return posting;
  }

  /**
   * Converts ArtistDto data to the model.
   *
   * @param data: data from ArtistDto.
   * @return the Artist derived from ArtistDto data.
   */
  Artist convertToModel(ArtistDto data) {
    if (data == null) return null;
    String email = data.getEmail();
    Artist artist = artistRepository.findArtistByEmail(email);

    if (artist == null) {
      String name = data.getName();
      String password = data.getPassword();
      Gallery gallery = convertToModel(data.getGallery());

      artist = new Artist();
      artist.setEmail(email);
      artist.setGallery(gallery);
      artist.setName(name);
      artist.setPassword(password);
    }

    return artist;
  }

  /**
   * Converts GalleryDto data to the model.
   *
   * @param data: data from GalleryDto.
   * @return the Gallery derived from GalleryDto data.
   */
  Gallery convertToModel(GalleryDto data) {
    if (data == null) return null;
    String name = data.getName();
    Gallery gallery = galleryRepository.findGalleryByName(name);
    if (gallery == null) {
      String city = data.getCity();
      double commission = data.getCommission();
      gallery = new Gallery();
      gallery.setName(name);
      gallery.setCity(city);
      gallery.setCommission(commission);
    }

    return gallery;
  }

  <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
