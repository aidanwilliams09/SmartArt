package ca.mcgill.ecse321.smartart.integration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import java.sql.Date;
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
import ca.mcgill.ecse321.smartart.dao.PostingRepository;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.dto.PostingDto;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.service.ArtistService;
import ca.mcgill.ecse321.smartart.service.GalleryService;
import ca.mcgill.ecse321.smartart.service.PostingService;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = SmartArtApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestPostingRest {

  @LocalServerPort private int port = 8080;

  @Autowired private TestRestTemplate restTemplate;

  private HttpHeaders headers = new HttpHeaders();

  @Autowired private PostingRepository postingRepository;
  @Autowired private ArtistRepository artistRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private PostingService postingService;
  @Autowired private GalleryService galleryService;
  @Autowired private ArtistService artistService;

  @Before
  @After
  public void clearDatabase() {
    galleryRepository.deleteAll();
    artistRepository.deleteAll();
    postingRepository.deleteAll();
  }

  @Test
  public void createPosting() {

    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    ArtistDto artist = new ArtistDto();
    artist.setEmail("aidan@mail.com");
    artist.setGallery(gallery);
    artist.setName("Aidan");
    artist.setPassword("pass");
    artistService.createArtist(artist);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();

    posting.setTitle("Test");
    posting.setDescription("Description");
    posting.setGallery(gallery);
    posting.setArtist(artist);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setImage("http://picturetest.com");

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    String result = response.getBody();
    assertTrue(result.contains("\"title\":\"Test\""));
  }

  @Test
  public void createPostingMissingArtist() {
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();

    posting.setTitle("Test");
    posting.setDescription("Description");
    posting.setGallery(gallery);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setImage("http://picturetest.com");

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    String result = response.getBody();
    assertTrue(result.contains("Posting artist cannot be empty."));
  }

  @Test
  public void createPostingMissingTitle() {
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    ArtistDto artist = new ArtistDto();
    artist.setEmail("aidan@mail.com");
    artist.setName("Aidan");
    artist.setPassword("pass");
    artist.setGallery(gallery);
    artistService.createArtist(artist);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();

    posting.setDescription("Description");
    posting.setArtist(artist);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setGallery(gallery);
    posting.setImage("http://picturetest.com");

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/create", HttpMethod.POST, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    String result = response.getBody();
    assertTrue(result.contains("Posting title cannot be empty"));
  }

  @Test
  public void updatePostingValidPosting() {
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    ArtistDto artist = new ArtistDto();
    artist.setEmail("aidan@mail.com");
    artist.setGallery(gallery);
    artist.setName("Aidan");
    artist.setPassword("pass");
    artistService.createArtist(artist);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();
    posting.setTitle("Test");
    posting.setDescription("Description");
    posting.setGallery(gallery);
    posting.setArtist(artist);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setImage("http://picturetest.com");
    Posting postingobj = postingService.createPosting(posting);

    posting.setTitle("New Title");
    posting.setDescription("New Description");
    posting.setImage("http://newimage.com");
    posting.setPrice(1000);
    posting.setXDim(50);
    posting.setYDim(60);
    posting.setZDim(70);
    posting.setPostingID(postingobj.getPostingID());

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/update", HttpMethod.PUT, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.OK, response.getStatusCode());

    String result = response.getBody();

    assertTrue(result.contains("\"title\":\"New Title\""));
    assertTrue(result.contains("\"description\":\"New Description\""));
    assertTrue(result.contains("\"image\":\"http://newimage.com\""));
    assertTrue(result.contains("\"price\":1000"));
    assertTrue(result.contains("\"xdim\":50"));
    assertTrue(result.contains("\"ydim\":60"));
    assertTrue(result.contains("\"zdim\":70"));
  }

  @Test
  public void updatePostingBadInputs() {
    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    ArtistDto artist = new ArtistDto();
    artist.setEmail("aidan@mail.com");
    artist.setGallery(gallery);
    artist.setName("Aidan");
    artist.setPassword("pass");
    artistService.createArtist(artist);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();
    posting.setTitle("Test");
    posting.setDescription("Description");
    posting.setGallery(gallery);
    posting.setArtist(artist);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setImage("http://picturetest.com");
    Posting postingobj = postingService.createPosting(posting);

    posting.setTitle(null);
    posting.setDescription(null);
    posting.setImage(null);
    posting.setPrice(0);
    posting.setXDim(0);
    posting.setYDim(0);
    posting.setZDim(0);
    posting.setPostingID(postingobj.getPostingID());

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/update", HttpMethod.PUT, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    String result = response.getBody();

    assertTrue(
        result.contains(
            "Posting must have a description. Posting must have an image. Posting must have a non-zero price. Posting must have a title. Posting must have positive dimensions."));
  }

  @Test
  public void updatePostingBadID() {

    GalleryDto gallery = new GalleryDto();
    gallery.setName("TestGallery");
    gallery.setCity("Montreal");
    gallery.setCommission(0.01);
    galleryService.createGallery(gallery);

    ArtistDto artist = new ArtistDto();
    artist.setEmail("aidan@mail.com");
    artist.setGallery(gallery);
    artist.setName("Aidan");
    artist.setPassword("pass");
    artistService.createArtist(artist);

    Date date = new Date(0);
    PostingDto posting = new PostingDto();
    posting.setTitle("Test");
    posting.setDescription("Description");
    posting.setGallery(gallery);
    posting.setArtist(artist);
    posting.setDate(date);
    posting.setPrice(123);
    posting.setXDim(10);
    posting.setYDim(10);
    posting.setZDim(1);
    posting.setImage("http://picturetest.com");

    posting.setPostingID(010101010);

    HttpEntity<PostingDto> entity = new HttpEntity<>(posting, headers);

    ResponseEntity<String> response =
        restTemplate.exchange(
            "http://localhost:8080/posting/update", HttpMethod.PUT, entity, String.class);
    // Check Status
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    String result = response.getBody();

    assertTrue(result.contains("No posting to update."));
  }
}
