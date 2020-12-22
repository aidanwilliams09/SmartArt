package ca.mcgill.ecse321.smartart.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.smartart.model.*;

/** @author Group 12 Repository for methods used by application such as getters and setters. */
@Repository
public class SmartArtRepository {

  @Autowired EntityManager entityManager;

  /**
   * @param name: Name of the Gallery.
   * @param city: City in which the Gallery is located.
   * @param commission: Commission that is given to the vendor.
   * @return the created Gallery.
   */
  @Transactional
  public Gallery createGallery(String name, String city, double commission) {
    Gallery g = new Gallery();
    g.setName(name);
    g.setCity(city);
    g.setCommission(commission);
    entityManager.persist(g);
    return g;
  }

  /**
   * @param name: Name of the Gallery to get.
   * @return the Gallery retrieved.
   */
  @Transactional
  public Gallery getGallery(String name) {
    Gallery g = entityManager.find(Gallery.class, name);
    return g;
  }

  /**
   * @param name: Name of the Administrator created.
   * @param phone: Phone number of the Administrator created.
   * @param email: Email of the Administrator created.
   * @param password: Password of the Administrator created.
   * @param gallery: Gallery that the created Administrator is in charge of.
   * @return the created Administrator.
   */
  @Transactional
  public Administrator createAdministrator(
      String name, String email, String password, Gallery gallery) {
    Administrator a = new Administrator();
    a.setName(name);
    a.setEmail(email);
    a.setPassword(password);
    a.setGallery(gallery);
    entityManager.persist(a);
    return a;
  }

  /**
   * @param email: Email of the Administrator to get.
   * @return the Administrator retrieved.
   */
  @Transactional
  public Administrator getAdministrator(String email) {
    Administrator a = entityManager.find(Administrator.class, email);
    return a;
  }

  /**
   * @param name: Name of the Artist created.
   * @param phone: Phone number of the Artist created.
   * @param email: Email of the Artist created.
   * @param password: Password of the Artist created.
   * @param gallery: Gallery that the created Artist has made.
   * @return the created Artist.
   */
  @Transactional
  public Artist createArtist(String name, String email, String password, Gallery gallery) {
    Artist a = new Artist();
    a.setName(name);
    a.setEmail(email);
    a.setPassword(password);
    a.setGallery(gallery);
    entityManager.persist(a);
    return a;
  }

  /**
   * @param email: Email of the Artist to get.
   * @return the Artist retrieved.
   */
  @Transactional
  public Artist getArtist(String email) {
    Artist a = entityManager.find(Artist.class, email);
    return a;
  }

  /**
   * @param name: Name of the Buyer created.
   * @param phone: Phone number of the Buyer created.
   * @param email: Email of the Buyer created.
   * @param password: Password of the Buyer created.
   * @param gallery: Gallery that the created Buyer is interested in buying.
   * @return the created Buyer.
   */
  @Transactional
  public Buyer createBuyer(String name, String email, String password, Gallery gallery) {
    Buyer b = new Buyer();
    b.setName(name);
    b.setEmail(email);
    b.setPassword(password);
    b.setGallery(gallery);
    entityManager.persist(b);
    return b;
  }

  /**
   * @param email: Email of the Buyer to get.
   * @return the Buyer retrieved.
   */
  @Transactional
  public Buyer getBuyer(String email) {
    Buyer b = entityManager.find(Buyer.class, email);
    return b;
  }

  /**
   * @param title: Title of the Posting created.
   * @param price: Price of the Posting created.
   * @param postingID: ID of the Posting created.
   * @param artist: Artist of the Posting created.
   * @param gallery: Gallery of the Posting created.
   * @return the Posting created.
   */
  @Transactional
  public Posting createPosting(
      String title, int price, int postingID, Artist artist, Gallery gallery) {
    Posting p = new Posting();
    p.setTitle(title);
    p.setPrice(price);
    p.setPostingID(postingID);
    p.setArtist(artist);
    p.setGallery(gallery);
    entityManager.persist(p);
    return p;
  }

  /**
   * @param postingID: ID of the Posting to get.
   * @return the Posting retrieved.
   */
  @Transactional
  public Posting getPosting(int postingID) {
    Posting p = entityManager.find(Posting.class, postingID);
    return p;
  }

  /**
   * @param purchaseID: ID of purchase to be created.
   * @param buyer: Buyer of the purchase to be created.
   * @return the Purchase created.
   */
  @Transactional
  public Purchase createPurchase(int purchaseID, Buyer buyer) {
    Purchase p = new Purchase();
    p.setPurchaseID(purchaseID);
    p.setBuyer(buyer);
    entityManager.persist(p);
    return p;
  }

  /**
   * @param purchaseID: ID of the Purchase to get.
   * @return the Purchase retrieved.
   */
  @Transactional
  public Purchase getPurchase(int purchaseID) {
    Purchase p = entityManager.find(Purchase.class, purchaseID);
    return p;
  }

  /** Deletes all entities. */
  @Transactional
  public void deleteAll() {
    entityManager.clear();
  }

  /**
   * @param maxPrice: Upper bound of price desired for Postings.
   * @return List of Postings available under the maxPrice.
   */
  @Transactional
  public List<Posting> getPostingsUnderPrice(int maxPrice) {
    TypedQuery<Posting> q =
        entityManager.createQuery(
            "select p from Posting p where p.price < :maxPrice", Posting.class);
    q.setParameter("maxPrice", maxPrice);
    List<Posting> resultList = q.getResultList();
    return resultList;
  }
}
