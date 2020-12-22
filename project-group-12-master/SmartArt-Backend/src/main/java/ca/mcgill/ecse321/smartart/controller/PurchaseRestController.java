package ca.mcgill.ecse321.smartart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.PurchaseDto;
import ca.mcgill.ecse321.smartart.model.DeliveryType;
import ca.mcgill.ecse321.smartart.model.Purchase;
import ca.mcgill.ecse321.smartart.service.PurchaseService;

/** Writes Purchase data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class PurchaseRestController {
  @Autowired private PurchaseService purchaseService;
  @Autowired private RestControllerHelper controllerHelper;

  /**
   * Gets a list of all Purchases after an HTTP request and puts into HTTP response in JSON or XML.
   *
   * @return the list of Purchases as Dto.
   */
  @GetMapping(value = {"/purchases", "/puchases/"})
  public ResponseEntity<?> getAllPurchases() {
    List<PurchaseDto> purchases =
        purchaseService.getAllPurchases().stream()
            .map(p -> controllerHelper.convertToDto(p))
            .collect(Collectors.toList());
    return new ResponseEntity<>(purchases, HttpStatus.OK);
  }

  /**
   * Gets a Purchase by its purchase ID after an HTTP request and puts into the HTTP response as
   * JSON or XMl.
   *
   * @param purchaseID: the ID of the purchase.
   * @return the retrieved Purchase in Dto.
   */
  @GetMapping(value = {"/purchases/{purchaseID}", "/purchases/{purchaseID}/"})
  public ResponseEntity<?> getPurchaseByPurchaseID(@PathVariable("purchaseID") int purchaseID) {
    Purchase purchase = purchaseService.getPurchase(purchaseID);
    if (purchase != null) {
      return new ResponseEntity<>(controllerHelper.convertToDto(purchase), HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Gets a list of all Purchases by a Buyer after an HTTP request and puts into HTTP response as
   * JSON or XML.
   *
   * @param email: the email of the Buyer.
   * @return the list of the Buyer's Purchases in Dto.
   */
  @GetMapping(value = {"/purchases/buyer/{email}", "/purchases/buyer/{email}/"})
  public ResponseEntity<?> getPurchasesByBuyer(@PathVariable("email") String email) {
    try {
      List<PurchaseDto> purchases =
          purchaseService.getPurchasesByBuyer(email).stream()
              .map(p -> controllerHelper.convertToDto(p))
              .collect(Collectors.toList());
      return new ResponseEntity<>(purchases, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Gets the cart of a Buyer after an HTTP request and puts into the HTTP response as JSON or XMl.
   *
   * @param email: the email of the Buyer.
   * @return the cart of the Buyer in Dto.
   */
  @GetMapping(value = {"/purchases/cart/{email}", "/purchases/cart/{email}/"})
  public ResponseEntity<?> getCart(@PathVariable("email") String email) {
    Purchase cart = purchaseService.getCart(email);
    try {
      if (cart != null)
        return new ResponseEntity<>(controllerHelper.convertToDto(cart), HttpStatus.OK);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Creates a purchase from Dto data after an HTTP request and puts into an HTTP response as JSON
   * or XML.
   *
   * @param data: the data from PurchaseDto.
   * @return the Purchase in Dto.
   */
  @PostMapping(value = {"/purchase/create", "/purchase/create/"})
  public ResponseEntity<?> createPurchase(@RequestBody PurchaseDto data) {
    try {
      Purchase purchase = purchaseService.createPurchase(data);
      return new ResponseEntity<>(controllerHelper.convertToDto(purchase), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Makes a Purchase from Dto data and delivery type after an HTTP request and puts into HTTP
   * response as JSON or XML.
   *
   * @param data: the data from PurchaseDto.
   * @param deliveryType: the type of delivery needed for the Purchase.
   * @return the Purchase in Dto.
   */
  @PostMapping(value = {"/purchase/make/{deliveryType}", "/purchase/make/{deliveryType}/"})
  public ResponseEntity<?> makePurchase(
      @RequestBody PurchaseDto data, @PathVariable("deliveryType") DeliveryType deliveryType) {
    try {
      Purchase purchase = purchaseService.makePurchase(data, deliveryType);
      return new ResponseEntity<>(controllerHelper.convertToDto(purchase), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Cancels a Purchase using Dto data after an HTTP request and puts into HTTP response as JSON or
   * XMl.
   *
   * @param data: the data from PurchaseDto
   * @return the status of the cancellation.
   */
  @DeleteMapping(value = {"/purchase/cancel", "/purchase/cancel/"})
  public ResponseEntity<?> cancelPurchase(@RequestBody PurchaseDto data) {
    try {
      boolean canceled = purchaseService.cancelPurchase(data);
      if (canceled) return new ResponseEntity<>(HttpStatus.OK);
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Adds a Posting to a Buyer's cart after an HTTP request and puts into HTTP response as JSON or
   * XML.
   *
   * @param buyerData: the data from BuyerDto.
   * @param postingID: the ID of the Posting to add
   * @return the Buyer's cart in Dto.
   */
  @PostMapping(value = {"/purchase/cart/add/{postingID}", "/purchase/cart/add/{postingID}/"})
  public ResponseEntity<?> addToCart(
      @RequestBody BuyerDto buyerData, @PathVariable(name = "postingID") int postingID) {
    try {
      Purchase cart = purchaseService.addToCart(buyerData, postingID);
      return new ResponseEntity<>(controllerHelper.convertToDto(cart), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }
  
  @PostMapping(value = {"/android/addToCart/{postingID}", "/purchase/cart/add/{postingID}/"})
  public ResponseEntity<?> addToCart(@RequestParam String email, @PathVariable(name = "postingID") int postingID) {
    try {
      Purchase cart = purchaseService.addToCart(email, postingID);
      return new ResponseEntity<>(controllerHelper.convertToDto(cart), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Removes a Posting from a Buyer's cart after an HTTP request and puts into an HTTP request as
   * JSON or XML.
   *
   * @param buyerData: the data from BuyerDto.
   * @param postingID: the ID of the Posting.
   * @return the Buyer's cart in Dto.
   */
  @DeleteMapping(
      value = {"/purchase/cart/remove/{postingID}", "/purchase/cart/remove/{postingID}/"})
  public ResponseEntity<?> removeFromCart(
      @RequestBody BuyerDto buyerData, @PathVariable(name = "postingID") int postingID) {
    try {
      Purchase cart = purchaseService.removeFromCart(buyerData, postingID);
      return new ResponseEntity<>(controllerHelper.convertToDto(cart), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }
}
