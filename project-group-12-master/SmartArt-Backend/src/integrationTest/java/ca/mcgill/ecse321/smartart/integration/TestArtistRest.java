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
import ca.mcgill.ecse321.smartart.dao.ArtistRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestArtistRest {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Autowired private ArtistRepository artistRepository;
  @Autowired private GalleryRepository galleryRepository;

  @Autowired private GalleryService galleryService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    artistRepository.deleteAll();
  }

  @Test
  public void createArtist() {
    // create gallery for artist
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    ArtistDto artist = new ArtistDto();
    artist.setEmail("ben@mail.com");
    artist.setName("Ben");
    artist.setPassword("pass");
    artist.setGallery(gallery);

    HttpEntity<ArtistDto> entity = new HttpEntity<>(artist, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/artist/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    String result = response.getBody();
    // check that artist was returned
    assertTrue(result.contains("\"email\":\"ben@mail.com\""));
    // check association
    assertTrue(result.contains("\"name\":\"TestGallery\""));
  }

  @Test
  public void createArtistNoGallery() {
    // dto to be passed
    ArtistDto artist = new ArtistDto();
    artist.setEmail("ben@mail.com");
    artist.setName("Ben");
    artist.setPassword("pass");

    HttpEntity<ArtistDto> entity = new HttpEntity<>(artist, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/artist/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("Artist gallery cannot be empty"));
  }

  @Test
  public void createDuplicateArtist() {
    // create gallery for artist
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    Gallery modelGallery = galleryService.createGallery(gallery);

    // create original artist
    Artist artist = new Artist();
    artist.setEmail("ben@mail.com");
    artist.setName("Ben");
    artist.setPassword("pass");
    artist.setGallery(modelGallery);
    artistRepository.save(artist);

    // dto to be passed
    ArtistDto duplicate = new ArtistDto();
    duplicate.setEmail("ben@mail.com");
    duplicate.setName("Ben");
    duplicate.setPassword("pass");
    duplicate.setGallery(gallery);

    HttpEntity<ArtistDto> entity = new HttpEntity<>(duplicate, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/artist/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error message
    assertTrue(result.contains("An Artist with this email already exists"));
  }

  @Test
  public void createEmptyArtist() {
    // create gallery for artist
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    // persist gallery
    galleryService.createGallery(gallery);

    // dto to be passed
    ArtistDto artist = new ArtistDto();
    artist.setGallery(gallery);

    HttpEntity<ArtistDto> entity = new HttpEntity<>(artist, headers);
    // create response entity
    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/artist/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    String result = response.getBody();
    // check error messages
    assertTrue(result.contains("Artist email cannot be empty"));
    assertTrue(result.contains("Artist name cannot be empty"));
    assertTrue(result.contains("Artist password cannot be empty"));
  }
}
