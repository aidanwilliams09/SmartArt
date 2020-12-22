package ca.mcgill.ecse321.smartart.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.smartart.model.*;
import ca.mcgill.ecse321.smartart.dao.*;

@ExtendWith(MockitoExtension.class)
public class PostingServiceTest {

  @Mock private PostingRepository postingDao;
  @Mock private ArtistRepository artistDao;
  @Mock private GalleryRepository galleryDao;

  @InjectMocks private PostingService postingService;

  private static final String ARTIST_KEY = "TestArtist";
  private static final String GALLERY_KEY = "TestGallery";
  private static final int POSTING_KEY = 100;
  private static final int NONEXISTING_POSTING = -1;

  @BeforeEach
  public void setMockOutput() {
    lenient()
        .when(postingDao.findPostingByPostingID(anyInt()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(POSTING_KEY)) {
                Posting posting = new Posting();
                posting.setPostingID(POSTING_KEY);
                return posting;
              } else {
                return null;
              }
            });
    lenient()
        .when(postingDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Posting> listPostings = new ArrayList<Posting>();
              listPostings.add(postingDao.findPostingByPostingID(POSTING_KEY));
              return listPostings;
            });
    lenient()
        .when(artistDao.findArtistByEmail(anyString()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(ARTIST_KEY)) {
                Artist artist = new Artist();
                artist.setEmail(ARTIST_KEY);
                return artist;
              } else {
                return null;
              }
            });
    lenient()
        .when(galleryDao.findGalleryByName(anyString()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(GALLERY_KEY)) {
                Gallery gallery = new Gallery();
                gallery.setName(GALLERY_KEY);
                galleryDao.save(gallery);
                return gallery;
              } else {
                return null;
              }
            });
    // Whenever anything is saved, just return the parameter object
    Answer<?> returnParameterAsAnswer =
        (InvocationOnMock invocation) -> {
          return invocation.getArgument(0);
        };

    lenient().when(postingDao.save(any(Posting.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(artistDao.save(any(Artist.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(galleryDao.save(any(Gallery.class))).thenAnswer(returnParameterAsAnswer);
  }

  ////////////////////////////
  ////// Posting tests/////////
  ////////////////////////////

  @Test
  public void testCreatePosting() {
    int postingID = 984532;
    Gallery gallery = new Gallery();
    Artist artist = new Artist();
    gallery.addArtist(artist);
    int price = 100;
    double x = 1;
    double y = 1;
    double z = 1;
    String title = "Moon";
    String description = "This is a moon";
    Date date = new Date(0);
    String url = "fakeimage";
    Posting posting = null;

    try {
      posting =
          postingService.createPosting(
              postingID, artist, price, x, y, z, title, description, date, url);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(posting);
    assertEquals(price, posting.getPrice());
  }

  @Test
  public void testCreatePostingNull() {

    int postingID = 984532;
    Artist artist = null;
    int price = 100;
    double x = 1;
    double y = 1;
    double z = 1;
    String title = "Moon";
    String description = "This is a moon";
    Date date = new Date(0);
    String url = "fakeimage";
    Posting posting = null;
    String error = null;

    try {
      posting =
          postingService.createPosting(
              postingID, artist, price, x, y, z, title, description, date, url);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(posting);
    assertEquals(error, "Posting artist cannot be empty.");
  }

  @Test
  public void testCreatePostingPriceLessThanZero() {

    int postingID = 984532;
    Gallery gallery = new Gallery();
    Artist artist = new Artist();
    gallery.addArtist(artist);
    int price = -100;
    double x = 1;
    double y = 1;
    double z = 1;
    String title = "Moon";
    String description = "This is a moon";
    Date date = new Date(0);
    String url = "fakeimage";
    Posting posting = null;
    String error = null;

    try {
      posting =
          postingService.createPosting(
              postingID, artist, price, x, y, z, title, description, date, url);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(posting);
    assertEquals(error, "Posting price must be above 0.");
  }

  @Test
  public void testGetExistingPosting() {
    assertEquals(POSTING_KEY, postingService.getPosting(POSTING_KEY).getPostingID());
  }

  @Test
  public void testGetNonExistingPosting() {
    assertNull(postingService.getPosting(NONEXISTING_POSTING));
  }

  @Test
  public void testDeleteExistingPosting() {
    Posting posting = postingService.getPosting(POSTING_KEY);
    Artist artist = new Artist();
    artist.setEmail(ARTIST_KEY);
    posting.setArtist(artist);
    Gallery gallery = new Gallery();
    gallery.setName(GALLERY_KEY);
    gallery.addPosting(posting);
    assertEquals(posting.getPostingID(), postingService.deletePosting(posting).getPostingID());
  }

  @Test
  public void testDeleteNonExistingPosting() {
    Posting posting = postingService.getPosting(NONEXISTING_POSTING);
    String error = null;
    try {
      postingService.deletePosting(posting);
    } catch (NullPointerException e) {
      error = e.getMessage();
    }
    assertNull(postingService.getPosting(NONEXISTING_POSTING));
    assertEquals(error, "Cannot remove null posting, perhaps it has already been deleted");
  }

  @Test
  public void testGetAllPostings() {
    assertEquals(POSTING_KEY, postingService.getAllPostings().get(0).getPostingID());
  }
}
