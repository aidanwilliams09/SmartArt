package ca.mcgill.ecse321.smartart.model;

import javax.persistence.OneToMany;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.ManyToOne;

import java.time.LocalDateTime;

/**
 * @author Group 12 This class represents a purchase that can be made for an art piece through a
 *     Posting in a Gallery.
 */
@Entity
public class Purchase {

  @Id
  @Column(name = "purchaseID")
  private int purchaseID;

  @ManyToOne(optional = false)
  private Buyer buyer;

  @Column(name = "totalPrice")
  private int totalPrice;

  @OneToMany private Set<Posting> postings;

  @Column(name = "deliveryType")
  private DeliveryType deliveryType;

  @Column(name = "timeOfPurchase")
  private LocalDateTime timeOfPurchase;

  public void setTime(LocalDateTime t) {
    this.timeOfPurchase = t;
  }

  public LocalDateTime getTime() {
    return this.timeOfPurchase;
  }

  /**
   * Gets the set of Postings of this Purchase.
   *
   * @return
   */
  public Set<Posting> getPostings() {
    return this.postings;
  }

  /**
   * Sets a new set of Postings for this Purchase.
   *
   * @param postings: the new set of Postings.
   */
  public void setPostings(Set<Posting> postings) {
    this.postings = postings;
  }

  /**
   * Adds a Posting to the set of Postings of this Purchase.
   *
   * @param posting: the Posting to be added.
   */
  public void addPosting(Posting posting) {
    if (this.postings == null) this.postings = new HashSet<Posting>();
    this.postings.add(posting);
    this.totalPrice += posting.getPrice();
  }

  /**
   * Removes a Posting from the set of Postings of this Purchase.
   *
   * @param posting: the Posting to be removed.
   * @return
   */
  public boolean removePosting(Posting posting) {
    if (this.postings.remove(posting)) {
      this.totalPrice -= posting.getPrice();
      return true;
    }
    return false;
  }

  /**
   * Checks if the Purchase has Postings.
   *
   * @return trueif there are Postings, false if not.
   */
  public boolean hasPostings() {
    if (this.postings == null) return false;
    return true;
  }

  /**
   * Gets the Buyer of this Purchase.
   *
   * @return the Buyer of this Purchase.
   */
  public Buyer getBuyer() {
    return this.buyer;
  }

  /**
   * Sets the Buyer of this Purchase.
   *
   * @param buyer: the Buyer of this Purchase.
   */
  public void setBuyer(Buyer buyer) {
    this.buyer = buyer;
  }

  /**
   * Sets the total price of this Purchase.
   *
   * @param value: the total price of this Purchase.
   */
  public void setTotalPrice(int value) {
    this.totalPrice = value;
  }

  /**
   * Gets the total price of this Purchase.
   *
   * @return the total price of this Purchase.
   */
  public int getTotalPrice() {
    return this.totalPrice;
  }

  /**
   * Sets the purchase ID for this Purchase.
   *
   * @param value: the purchase ID for this Purchase.
   */
  public void setPurchaseID(int value) {
    this.purchaseID = value;
  }

  /**
   * Gets the purchase ID of this Purchase.
   *
   * @return the purchase ID of this Purchase.
   */
  public int getPurchaseID() {
    return this.purchaseID;
  }

  /**
   * Sets the delivery type of this Purchase.
   *
   * @param deliveryType: the delivery type of this Purchase.
   */
  public void setDeliveryType(DeliveryType deliveryType) {
    this.deliveryType = deliveryType;
  }

  /**
   * Gets the delivery type of this Purchase.
   *
   * @return the delivery type of this Purchase.
   */
  public DeliveryType getDeliveryType() {
    return this.deliveryType;
  }
}
