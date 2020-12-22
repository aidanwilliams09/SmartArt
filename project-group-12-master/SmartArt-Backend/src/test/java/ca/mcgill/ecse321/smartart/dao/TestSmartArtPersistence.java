package ca.mcgill.ecse321.smartart.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.mcgill.ecse321.smartart.model.*;

/** @author Group 12 Class used to test Smart Art persistence. */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestSmartArtPersistence {

  /** Repositories to use for database purposes. */
  @Autowired private ArtistRepository artistRepository;

  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private PostingRepository postingRepository;
  @Autowired private PurchaseRepository purchaseRepository;

  /** Creates a gallery with a name, city and commission value. */
  @BeforeEach
  public void createGallery() {
    String galName = "Guginhiem";
    String city = "bilbao";
    double commission = 0.2;

    Gallery gallery = new Gallery();
    gallery.setName(galName);
    gallery.setCity(city);
    gallery.setCommission(commission);

    galleryRepository.save(gallery);
  }

  /** Clears all the repositories in the database. */
  @AfterEach
  public void clearDatabase() {
    galleryRepository.deleteAll();
    administratorRepository.deleteAll();
    artistRepository.deleteAll();
    buyerRepository.deleteAll();
    postingRepository.deleteAll();
    purchaseRepository.deleteAll();
  }

  /**
   * Tests the save function for the repositories and the findGalleryByName function to see if it
   * can persist and load information properly.
   */
  @Test
  public void testPersistAndLoadGallery() {
    String galName = "Guginhiem";
    String name = "ella";
    String email = "ella@mail.com";

    Artist artist = new Artist();
    artist.setName(name);
    artist.setEmail(email);

    Gallery gallery = galleryRepository.findGalleryByName(galName);

    gallery.addArtist(artist);

    galleryRepository.save(gallery);
    artistRepository.save(artist);

    gallery = null;

    gallery = galleryRepository.findGalleryByName(galName);

    assertNotNull(gallery);
    assertEquals(galName, gallery.getName());
    assertNotNull(gallery.getArtists());
    assertEquals(name, ((Artist) gallery.getArtists().toArray()[0]).getName());
  }

  /**
   * Tests to see if the repositories can store the information given with the save function and
   * tests if findArtistByEmail can load information from the repositories.
   */
  @Test
  public void testPersistAndLoadArtist() {
    String email = "mike@mail.com";
    String name = "mike";
    String pw = "abc123";
    String galName = "Guginhiem";

    Artist a = new Artist();
    a.setName(name);
    a.setEmail(email);
    a.setPassword(pw);

    Gallery g = galleryRepository.findGalleryByName(galName);
    g.addArtist(a);

    artistRepository.save(a);
    galleryRepository.save(g);

    a = null;

    a = artistRepository.findArtistByEmail(email);

    assertNotNull(a);
    assertEquals(email, a.getEmail());
    assertEquals(g.getName(), a.getGallery().getName());
  }

  /**
   * Tests the save function for the repositories and the findBuyerByEmail function to see if it can
   * persist and load information properly.
   */
  @Test
  public void testPersistAndLoadBuyer() {
    String email = "bob@mail.com";
    String name = "bob";
    String pw = "abc123";
    String galName = "Guginhiem";

    Buyer b = new Buyer();
    b.setName(name);
    b.setEmail(email);
    b.setPassword(pw);

    Gallery g = galleryRepository.findGalleryByName(galName);
    g.addBuyer(b);

    galleryRepository.save(g);
    buyerRepository.save(b);

    b = null;

    b = buyerRepository.findBuyerByEmail(email);

    assertNotNull(b);
    assertEquals(email, b.getEmail());
    assertEquals(g.getName(), b.getGallery().getName());
  }

  /**
   * Tests the save function for the repositories and the findAdministratorByEmail function to see
   * if it can persist and load information properly.
   */
  @Test
  public void testPersistAndLoadAdministrator() {
    String email = "pam@mail.com";
    String name = "pam";
    String pw = "abc123";
    String galName = "Guginhiem";

    Administrator a = new Administrator();
    a.setName(name);
    a.setEmail(email);
    a.setPassword(pw);

    Gallery g = galleryRepository.findGalleryByName(galName);
    g.addAdministrator(a);

    galleryRepository.save(g);
    administratorRepository.save(a);

    a = null;

    a = administratorRepository.findAdministratorByEmail(email);

    assertNotNull(a);
    assertEquals(email, a.getEmail());
    assertEquals(g.getName(), a.getGallery().getName());
  }

  /**
   * Tests the save function for the repositories and the findPostingByPostingID function to see if
   * it can persist and load information properly.
   */
  @Test
  public void testPersistAndLoadPosting() {
    int id = 7464;
    String title = "Nu couche";
    int price = 100000000;
    String email = "amy@mail.com";
    String name = "amy";
    String galName = "Guginhiem";
    ArtStatus artStatus = ArtStatus.Available;

    Gallery g = galleryRepository.findGalleryByName(galName);

    Artist a = new Artist();
    a.setName(name);
    a.setEmail(email);

    Posting p = new Posting();
    p.setPostingID(id);
    p.setTitle(title);
    p.setPrice(price);
    p.setArtStatus(artStatus);

    g.addArtist(a);

    a.addPosting(p);

    g.addPosting(p);

    artistRepository.save(a);
    postingRepository.save(p);
    galleryRepository.save(g);

    p = null;

    p = postingRepository.findPostingByPostingID(id);

    assertNotNull(p);
    assertEquals(name, p.getArtist().getName());
    assertEquals(price, p.getPrice());
    assertEquals(artStatus, p.getArtStatus());
  }

  /**
   * Tests the save function for the repositories and the findPurchaseByPurchaseID function to see
   * if it can persist and load information properly.
   */
  @Test
  public void testPersistAndLoadPurchase() {
    int id = 9874;
    String email = "meg@mail.com";
    String name = "meg";
    String galName = "Guginhiem";
    LocalDateTime now = LocalDateTime.now();
    DeliveryType deliveryType = DeliveryType.PickUp;

    Gallery g = galleryRepository.findGalleryByName(galName);

    Buyer b = new Buyer();
    b.setName(name);
    b.setEmail(email);
    b.setGallery(g);

    Purchase p = new Purchase();
    p.setPurchaseID(id);
    p.setTotalPrice(0);
    p.setDeliveryType(deliveryType);
    p.setTime(now);

    b.addPurchase(p);

    buyerRepository.save(b);
    purchaseRepository.save(p);

    p = null;

    p = purchaseRepository.findPurchaseByPurchaseID(id);

    assertNotNull(p);
    assertEquals(name, p.getBuyer().getName());
    assertEquals(id, p.getPurchaseID());
    assertEquals(deliveryType, p.getDeliveryType());
  }
}
