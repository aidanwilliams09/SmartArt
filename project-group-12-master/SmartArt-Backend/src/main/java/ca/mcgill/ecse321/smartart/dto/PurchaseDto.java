package ca.mcgill.ecse321.smartart.dto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import ca.mcgill.ecse321.smartart.model.DeliveryType;

public class PurchaseDto {

  private int purchaseID;
  private BuyerDto buyer;
  private int totalPrice;
  private List<PostingDto> postings;
  private DeliveryType deliveryType;
  private LocalDateTime time;

  public PurchaseDto() {}

  public PurchaseDto(BuyerDto buyer) {
    this(0000, buyer, 0, Collections.emptyList(), null, null);
  }

  public PurchaseDto(
      int purchaseID,
      BuyerDto buyer,
      int totalPrice,
      List<PostingDto> postings,
      DeliveryType deliveryType,
      LocalDateTime time) {
    this.purchaseID = purchaseID;
    this.buyer = buyer;
    this.totalPrice = totalPrice;
    this.postings = postings;
    this.deliveryType = deliveryType;
    this.time = time;
  }

  public List<PostingDto> getPostings() {
    return this.postings;
  }

  public void setPostings(List<PostingDto> postings) {
    this.postings = postings;
  }

  public BuyerDto getBuyer() {
    return this.buyer;
  }

  public void setBuyer(BuyerDto buyer) {
    this.buyer = buyer;
  }

  public void setTotalPrice(int value) {
    this.totalPrice = value;
  }

  public int getTotalPrice() {
    return this.totalPrice;
  }

  public void setPurchaseID(int value) {
    this.purchaseID = value;
  }

  public int getPurchaseID() {
    return this.purchaseID;
  }

  public void setDeliveryType(DeliveryType deliveryType) {
    this.deliveryType = deliveryType;
  }

  public DeliveryType getDeliveryType() {
    return this.deliveryType;
  }

  public void setTime(LocalDateTime time) {
    this.time = time;
  }

  public LocalDateTime getTime() {
    return this.time;
  }
}
