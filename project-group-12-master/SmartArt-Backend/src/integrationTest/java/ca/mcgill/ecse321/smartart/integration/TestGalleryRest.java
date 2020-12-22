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
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Gallery;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestGalleryRest {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Autowired private GalleryRepository galleryRepository;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
  }

  @Test
  public void createGallery() {
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);

    HttpEntity<GalleryDto> entity = new HttpEntity<>(gallery, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/gallery/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String result = response.getBody();
    // check that gallery was returned
    assertTrue(result.contains("\"name\":\"TestGallery\""));
  }

  @Test
  public void createDuplicateGallery() {
    // create original
    Gallery gallery = new Gallery();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryRepository.save(gallery);

    // create duplicate
    GalleryDto duplicate = new GalleryDto();
    duplicate.setName("TestGallery");
    duplicate.setCity("Montreal");
    duplicate.setCommission(0.01);

    HttpEntity<GalleryDto> entity = new HttpEntity<>(duplicate, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/gallery/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("A Gallery by this name already exists"));
  }

  @Test
  public void createEmptyGallery() {

    GalleryDto gallery = new GalleryDto();

    HttpEntity<GalleryDto> entity = new HttpEntity<>(gallery, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/gallery/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error messages
    assertTrue(result.contains("Gallery name cannot be empty"));
    assertTrue(result.contains("Gallery city cannot be empty"));
  }
}
