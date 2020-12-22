package ca.mcgill.ecse321.smartart.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.smartart.SmartArtApplication;
import ca.mcgill.ecse321.smartart.dao.*;
import ca.mcgill.ecse321.smartart.model.*;
import ca.mcgill.ecse321.smartart.service.AdministratorService;
import ca.mcgill.ecse321.smartart.service.ArtistService;
import ca.mcgill.ecse321.smartart.service.BuyerService;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import ca.mcgill.ecse321.smartart.service.PostingService;
import ca.mcgill.ecse321.smartart.service.PurchaseService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestGetEndPoints {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private ArtistRepository artistRepository;
  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private PostingRepository postingRepository;
  @Autowired private PurchaseRepository purchaseRepository;

  @Autowired private AdministratorService administratorService;
  @Autowired private ArtistService artistService;
  @Autowired private BuyerService buyerService;
  @Autowired private GalleryService galleryService;
  @Autowired private PostingService postingService;
  @Autowired private PurchaseService purchaseService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    administratorRepository.deleteAll();
    artistRepository.deleteAll();
    buyerRepository.deleteAll();
    postingRepository.deleteAll();
    purchaseRepository.deleteAll();
  }

  @Before
  public void setUpTests() {
    Gallery gallery = galleryService.createGallery("Gallery", "Montreal", 0.05);
    Artist artist = artistService.createArtist("ben@mail.com", "Ben", "abc123", gallery);
    administratorService.createAdministrator("greg@mail.com", "Greg", "abc123", gallery);
    Buyer buyer = buyerService.createBuyer("aidan@mail.com", "Aidan", "abc123", gallery);
    postingService.createPosting(
        124344, artist, 123, 1, 1, 1, "Art", "This is Art", new Date(0), "fakeimage");
    ;
    Purchase purchase = purchaseService.createPurchase(21122, buyer);
    buyer.setCart(purchase);
    buyerRepository.save(buyer);
  }

  @Test
  public void getGalleries() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/galleries", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that gallery is correctly returned
    assertTrue(result.contains("\"name\":\"Gallery\""));
    // check attribute
    assertTrue(result.contains("\"city\":\"Montreal\""));
  }

  @Test
  public void getGalleryByID() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/galleries/Gallery", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that gallery is correctly returned
    assertTrue(result.contains("\"name\":\"Gallery\""));
    // check attribute
    assertTrue(result.contains("\"city\":\"Montreal\""));
  }

  @Test
  public void getArtists() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange("http://localhost:8080/artists", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that artist is returned correctly
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getArtistByEmail() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/artists/ben@mail.com", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getAdministrators() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrators", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that administrator is returned correctly
    assertTrue(result.contains("\"email\":\"greg@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getAdministratorByEmail() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrators/greg@mail.com",
            HttpMethod.GET,
            null,
            String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that administrator is returned correctly
    assertTrue(result.contains("\"email\":\"greg@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getBuyers() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange("http://localhost:8080/buyers", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that buyer is returned correctly
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getBuyerByEmail() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/buyers/aidan@mail.com", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that buyer is returned correctly
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
    // check gallery association
    assertTrue(result.contains("\"name\":\"Gallery\""));
  }

  @Test
  public void getPostings() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange("http://localhost:8080/postings", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that posting is correctly returned
    assertTrue(result.contains("\"postingID\":124344"));
    // check  artist association
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
  }

  @Test
  public void getPostingByID() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/postings/124344", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that posting is correctly returned
    assertTrue(result.contains("\"postingID\":124344"));
    // check artist association
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
  }

  @Test
  public void getPostingsByArtist() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/postings/artist/ben@mail.com",
            HttpMethod.GET,
            null,
            String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that posting is correctly returned
    assertTrue(result.contains("\"postingID\":124344"));
    // check artist association
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
  }

  @Test
  public void getPurchases() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchases", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that purchase is returned correctly
    assertTrue(result.contains("\"purchaseID\":21122"));
    // check buyer association
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
  }

  @Test
  public void getPurchaseByID() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchases/21122", HttpMethod.GET, null, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that purchase is returned correctly
    assertTrue(result.contains("\"purchaseID\":21122"));
    // check buyer association
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
  }

  @Test
  public void getPurchasesByBuyer() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchases/buyer/aidan@mail.com",
            HttpMethod.GET,
            null,
            String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that purchase is returned correctly
    assertTrue(result.contains("\"purchaseID\":21122"));
    // check buyer association
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
  }

  @Test
  public void getCart() {
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchases/cart/aidan@mail.com",
            HttpMethod.GET,
            null,
            String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that purchase is returned correctly
    assertTrue(result.contains("\"purchaseID\":21122"));
    // check buyer association
    assertTrue(result.contains("\"email\":\"aidan@mail.com\""));
  }
}
