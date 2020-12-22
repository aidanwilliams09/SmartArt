package ca.mcgill.ecse321.smartart.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.smartart.SmartArtApplication;
import ca.mcgill.ecse321.smartart.dao.ArtistRepository;
import ca.mcgill.ecse321.smartart.dao.BuyerRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dao.PostingRepository;
import ca.mcgill.ecse321.smartart.dao.PurchaseRepository;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.dto.PostingDto;
import ca.mcgill.ecse321.smartart.dto.PurchaseDto;
import ca.mcgill.ecse321.smartart.model.ArtStatus;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.DeliveryType;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.model.Purchase;
import ca.mcgill.ecse321.smartart.service.ArtistService;
import ca.mcgill.ecse321.smartart.service.BuyerService;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import ca.mcgill.ecse321.smartart.service.PostingService;
import ca.mcgill.ecse321.smartart.service.PurchaseService;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestPurchaseRest {

  @LocalServerPort private final int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private final HttpHeaders headers = new HttpHeaders();

  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private PurchaseRepository purchaseRepository;
  @Autowired private ArtistRepository artistRepository;
  @Autowired private PostingRepository postingRepository;

  @Autowired private GalleryService galleryService;
  @Autowired private BuyerService buyerService;
  @Autowired private ArtistService artistService;
  @Autowired private PostingService postingService;
  @Autowired private PurchaseService purchaseService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    buyerRepository.deleteAll();
    artistRepository.deleteAll();
    purchaseRepository.deleteAll();
    postingRepository.deleteAll();
  }

  @Test
  public void createPurchase() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    galleryService.createGallery(gallery);

    // create buyer for purchase
    BuyerDto buyer = new BuyerDto("test@mail.com", gallery);
    // persist buyer
    buyerService.createBuyer(buyer);

    // dto to be passed
    PurchaseDto purchase = new PurchaseDto(buyer);

    HttpEntity<PurchaseDto> entity = new HttpEntity<>(purchase, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/create", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String result = response.getBody();
    // check association to buyer
    assertTrue(result.contains("\"email\":\"test@mail.com\""));
  }

  @Test
  public void createPurchaseNoBuyer() {
    // dto to be passed
    PurchaseDto purchase = new PurchaseDto();
    purchase.setPurchaseID(123);

    HttpEntity<PurchaseDto> entity = new HttpEntity<>(purchase, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/create", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("Purchase buyer cannot be empty"));
  }

  @Test
  public void addToCart() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.Available);
    postingRepository.save(posting);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/add/123", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    int purchaseID = buyerRepository.findBuyerByEmail("buyer@mail.com").getCart().getPurchaseID();
    String pID = "";
    pID += purchaseID;
    // check that proper association was formed
    assertTrue(result.contains(pID));
    // check that posting was added
    assertTrue(result.contains("\"postingID\":123"));
    // check that price was updated
    assertTrue(result.contains("\"totalPrice\":5"));
  }

  @Test
  public void addToCartInvalidBuyer() {

    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto();
    buyer.setEmail("notABuyer@mail.com");

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.Available);
    postingRepository.save(posting);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/add/123", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("Buyer does not exist"));
  }

  @Test
  public void addToCartInvalidPosting() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    artistService.createArtist(artist);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/add/123", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error
    assertTrue(result.contains("Posting does not exist"));
  }

  @Test
  public void addToCartNotAvailable() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.OnHold);
    postingRepository.save(posting);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/add/123", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error
    assertTrue(result.contains("addToCart posting cannot be On Hold or Purchased"));
  }

  @Test
  public void makePurchase() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    artistService.createArtist(artist);

    // create posting
    PostingDto posting = new PostingDto(artist);
    // persist posting
    Posting modelPosting = postingService.createPosting(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, modelPosting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    HttpEntity<PurchaseDto> entity = new HttpEntity<>(cart, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/make/PickUp", HttpMethod.POST, entity, String.class);
    // check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check buyer association
    assertTrue(result.contains("\"email\":\"buyer@mail.com\""));
    // check art status of posting
    assertTrue(result.contains("\"artStatus\":\"Purchased\""));
    // check final price
    int finalPrice = (int) ((gallery.getCommission() + 1) * posting.getPrice());
    String check = "\"totalPrice\":" + finalPrice;
    assertTrue(result.contains(check));
  }

  @Test
  public void cancelPurchase() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    artistService.createArtist(artist);

    // create posting
    PostingDto posting = new PostingDto(artist);
    // persist posting
    Posting modelPosting = postingService.createPosting(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, modelPosting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    // execute purchase
    purchaseService.makePurchase(cart, DeliveryType.PickUp);

    HttpEntity<PurchaseDto> entity = new HttpEntity<>(cart, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cancel", HttpMethod.DELETE, entity, String.class);
    // check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    int id = modelPosting.getPostingID();
    modelPosting = postingRepository.findPostingByPostingID(id);
    assertEquals(modelPosting.getArtStatus(), ArtStatus.Available);
  }

  @Test
  public void cancelPurchaseTooLate() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    artistService.createArtist(artist);

    // create posting
    PostingDto posting = new PostingDto(artist);
    // persist posting
    Posting modelPosting = postingService.createPosting(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, modelPosting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    // execute purchase
    Purchase modelPurchase = purchaseService.makePurchase(cart, DeliveryType.PickUp);

    LocalDateTime now = LocalDateTime.now();
    cart.setTime(now.minusMinutes(15));
    modelPurchase.setTime(now.minusMinutes(15));
    purchaseRepository.save(modelPurchase);

    HttpEntity<PurchaseDto> entity = new HttpEntity<>(cart, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cancel", HttpMethod.DELETE, entity, String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void removeFromCart() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.Available);
    postingRepository.save(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, posting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/remove/123",
            HttpMethod.DELETE,
            entity,
            String.class);
    // check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String result = response.getBody();
    // check that cart is empty
    assertTrue(result.contains("\"postings\":[]"));
    // check art status
    int id = posting.getPostingID();
    posting = postingRepository.findPostingByPostingID(id);
    assertEquals(posting.getArtStatus(), ArtStatus.Available);
  }

  @Test
  public void removeFromCartInvalidBuyer() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.Available);
    postingRepository.save(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, posting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    BuyerDto invalidBuyer = new BuyerDto();
    invalidBuyer.setEmail("notABuyer");

    HttpEntity<BuyerDto> entity = new HttpEntity<>(invalidBuyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/remove/123",
            HttpMethod.DELETE,
            entity,
            String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    assertTrue(result.contains("Invalid buyer"));
  }

  @Test
  public void removeFromCartInvalidPosting() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto("TestGallery");
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create buyer
    BuyerDto buyer = new BuyerDto("buyer@mail.com", gallery);
    // persist buyer
    Buyer modelBuyer = buyerService.createBuyer(buyer);

    // create artist
    ArtistDto artist = new ArtistDto("artist@mail.com", gallery);
    // persist artist
    Artist modelArtist = artistService.createArtist(artist);

    // create and persist posting as model
    Posting posting = new Posting();
    posting.setPostingID(123);
    posting.setGallery(modelGallery);
    posting.setArtist(modelArtist);
    posting.setPrice(5);
    posting.setArtStatus(ArtStatus.Available);
    postingRepository.save(posting);

    // add posting to buyer cart
    purchaseService.addToCart(modelBuyer, posting);

    PurchaseDto cart = new PurchaseDto();
    cart.setBuyer(buyer);
    cart.setPurchaseID(modelBuyer.getCart().getPurchaseID());

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/purchase/cart/remove/124",
            HttpMethod.DELETE,
            entity,
            String.class);
    // check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    assertTrue(result.contains("Invalid posting"));
  }
}
