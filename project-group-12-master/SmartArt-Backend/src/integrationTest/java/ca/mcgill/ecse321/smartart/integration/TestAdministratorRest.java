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
import ca.mcgill.ecse321.smartart.dao.AdministratorRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.AdministratorDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdministratorRest {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private GalleryRepository galleryRepository;

  @Autowired private GalleryService galleryService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    administratorRepository.deleteAll();
  }

  @Test
  public void createAdministrator() {
    // create gallery for administrator
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    AdministratorDto administrator = new AdministratorDto();
    administrator.setEmail("ben@mail.com");
    administrator.setName("Ben");
    administrator.setPassword("pass");
    administrator.setGallery(gallery);

    HttpEntity<AdministratorDto> entity = new HttpEntity<>(administrator, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrator/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String result = response.getBody();
    // check that administrator was returned
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
    // check association
    assertTrue(result.contains("\"name\":\"TestGallery\""));
  }

  @Test
  public void createAdministratorNoGallery() {
    // dto to be passed
    AdministratorDto administrator = new AdministratorDto();
    administrator.setEmail("ben@mail.com");
    administrator.setName("Ben");
    administrator.setPassword("pass");

    HttpEntity<AdministratorDto> entity = new HttpEntity<>(administrator, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrator/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("Administrator gallery cannot be empty"));
  }

  @Test
  public void createDuplicateAdministrator() {
    // create gallery for administrator
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create original administrator
    Administrator administrator = new Administrator();
    administrator.setEmail("ben@mail.com");
    administrator.setName("Ben");
    administrator.setPassword("pass");
    administrator.setGallery(modelGallery);
    administratorRepository.save(administrator);

    // dto to be passed
    AdministratorDto duplicate = new AdministratorDto();
    duplicate.setEmail("ben@mail.com");
    duplicate.setName("Ben");
    duplicate.setPassword("pass");
    duplicate.setGallery(gallery);

    HttpEntity<AdministratorDto> entity = new HttpEntity<>(duplicate, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrator/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("An Administrator with this email already exists"));
  }

  @Test
  public void createEmptyAdministrator() {
    // create gallery for administrator
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    AdministratorDto administrator = new AdministratorDto();
    administrator.setGallery(gallery);

    HttpEntity<AdministratorDto> entity = new HttpEntity<>(administrator, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/administrator/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error messages
    assertTrue(result.contains("Administrator email cannot be empty"));
    assertTrue(result.contains("Administrator name cannot be empty"));
    assertTrue(result.contains("Administrator password cannot be empty"));
  }
}
