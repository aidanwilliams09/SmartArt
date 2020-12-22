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
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.smartart.model.*;
import ca.mcgill.ecse321.smartart.dao.*;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

  @Mock private ArtistRepository artistDao;
  @Mock private GalleryRepository galleryDao;

  @InjectMocks private ArtistService artistService;

  private static final String ARTIST_KEY = "TestArtist";
  private static final String NONEXISTING_ARTIST = "NotAnArtist";
  private static final String GALLERY_KEY = "TestGallery";

  @BeforeEach
  public void setMockOutput() {
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
        .when(artistDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Artist> listArtists = new ArrayList<Artist>();
              listArtists.add(artistDao.findArtistByEmail(ARTIST_KEY));
              return listArtists;
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
    lenient().when(artistDao.save(any(Artist.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(galleryDao.save(any(Gallery.class))).thenAnswer(returnParameterAsAnswer);
  }

  @Test
  public void testCreateArtist() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    Gallery gallery = new Gallery();
    Artist artist = null;
    try {
      artist = artistService.createArtist(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(artist);
    assertEquals(name, artist.getName());
  }

  @Test
  public void testCreateArtistNull() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = null;
    Artist artist = null;
    try {
      artist = artistService.createArtist(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(artist);
    // check error
    assertEquals("Artist gallery cannot be empty.", error);
  }

  @Test
  public void testCreateArtistEmpty() {
    String email = "";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Artist artist = null;
    try {
      artist = artistService.createArtist(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(artist);
    // check error
    assertEquals("Artist email cannot be empty.", error);
  }

  @Test
  public void testCreateArtistSpaces() {
    String email = "  ";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Artist artist = null;
    try {
      artist = artistService.createArtist(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(artist);
    // check error
    assertEquals("Artist email cannot be empty.", error);
  }

  @Test
  public void testGetExistingArtist() {
    assertEquals(ARTIST_KEY, artistService.getArtist(ARTIST_KEY).getEmail());
  }

  @Test
  public void testGetNonExistingArtist() {
    assertNull(artistService.getArtist(NONEXISTING_ARTIST));
  }

  @Test
  public void testGetAllArtists() {
    assertEquals(ARTIST_KEY, artistService.getAllArtists().get(0).getEmail());
  }
}
