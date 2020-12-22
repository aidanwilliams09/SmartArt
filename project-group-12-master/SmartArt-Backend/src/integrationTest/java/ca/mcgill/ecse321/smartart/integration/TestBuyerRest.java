package ca.mcgill.ecse321.smartart.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

import ca.mcgill.ecse321.smartart.SmartArtApplication;
import ca.mcgill.ecse321.smartart.dao.BuyerRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestBuyerRest {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;

  @Autowired private GalleryService galleryService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    buyerRepository.deleteAll();
  }

  @Test
  public void createBuyer() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    BuyerDto buyer = new BuyerDto();
    buyer.setEmail("ben@mail.com");
    buyer.setName("Ben");
    buyer.setPassword("pass");
    buyer.setGallery(gallery);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/buyer/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String result = response.getBody();
    // check that buyer was returned
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
    // check association
    assertTrue(result.contains("\"name\":\"TestGallery\""));
  }

  @Test
  public void createBuyerNoGallery() {
    // dto to be passed
    BuyerDto buyer = new BuyerDto();
    buyer.setEmail("ben@mail.com");
    buyer.setName("Ben");
    buyer.setPassword("pass");

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/buyer/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("Buyer gallery cannot be empty"));
  }

  @Test
  public void createDuplicateBuyer() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create original buyer
    Buyer buyer = new Buyer();
    buyer.setEmail("ben@mail.com");
    buyer.setName("Ben");
    buyer.setPassword("pass");
    buyer.setGallery(modelGallery);
    buyerRepository.save(buyer);

    // dto to be passed
    BuyerDto duplicate = new BuyerDto();
    duplicate.setEmail("ben@mail.com");
    duplicate.setName("Ben");
    duplicate.setPassword("pass");
    duplicate.setGallery(gallery);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(duplicate, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/buyer/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("A Buyer with this email already exists"));
  }

  @Test
  public void createEmptyBuyer() {
    // create gallery for buyer
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    BuyerDto buyer = new BuyerDto();
    buyer.setGallery(gallery);

    HttpEntity<BuyerDto> entity = new HttpEntity<>(buyer, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/buyer/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error messages
    assertTrue(result.contains("Buyer email cannot be empty"));
    assertTrue(result.contains("Buyer name cannot be empty"));
    assertTrue(result.contains("Buyer password cannot be empty"));
  }
}
