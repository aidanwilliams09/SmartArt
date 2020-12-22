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
public class GalleryServiceTest {

  @Mock private GalleryRepository galleryDao;

  @InjectMocks private GalleryService galleryService;

  private static final String GALLERY_KEY = "TestGallery";
  private static final String NONEXISTING_GALLERY = "NotAGallery";

  @BeforeEach
  public void setMockOutput() {

    lenient()
        .when(galleryDao.findGalleryByName(anyString()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(GALLERY_KEY)) {
                Gallery gallery = new Gallery();
                gallery.setName(GALLERY_KEY);
                return gallery;
              } else {
                return null;
              }
            });
    lenient()
        .when(galleryDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Gallery> listGallerys = new ArrayList<Gallery>();
              listGallerys.add(galleryDao.findGalleryByName(GALLERY_KEY));
              return listGallerys;
            });
    // Whenever anything is saved, just return the parameter object
    Answer<?> returnParameterAsAnswer =
        (InvocationOnMock invocation) -> {
          return invocation.getArgument(0);
        };
    lenient().when(galleryDao.save(any(Gallery.class))).thenAnswer(returnParameterAsAnswer);
  }

  ////////////////////////////
  ////// Gallery tests/////////
  ////////////////////////////

  @Test
  public void testCreateGallery() {
    String name = "Gugenhiem";
    String city = "Bilbao";
    double commission = 0.1;
    Gallery gallery = null;
    try {
      gallery = galleryService.createGallery(name, city, commission);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(gallery);
    assertEquals(name, gallery.getName());
  }

  @Test
  public void testCreateGalleryNull() {
    String name = null;
    String error = null;
    Gallery gallery = null;
    try {
      gallery = galleryService.createGallery(name, "Montreal", 0.05);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(gallery);
    // check error
    assertEquals("Gallery name cannot be empty.", error);
  }

  @Test
  public void testCreateGalleryEmpty() {
    String name = "";
    String error = null;
    Gallery gallery = null;
    try {
      gallery = galleryService.createGallery(name, "Montreal", 0.05);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(gallery);
    // check error
    assertEquals("Gallery name cannot be empty.", error);
  }

  @Test
  public void testCreateGallerySpaces() {
    String city = " ";
    String error = null;
    Gallery gallery = null;
    try {
      gallery = galleryService.createGallery("Gallery", city, 0.05);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(gallery);
    // check error
    assertEquals("Gallery city cannot be empty.", error);
  }

  @Test
  public void testGetExistingGallery() {
    assertEquals(GALLERY_KEY, galleryService.getGallery(GALLERY_KEY).getName());
  }

  @Test
  public void testGetNonExistingGallery() {
    assertNull(galleryService.getGallery(NONEXISTING_GALLERY));
  }

  @Test
  public void testGetAllGalleries() {
    assertEquals(GALLERY_KEY, galleryService.getAllGalleries().get(0).getName());
  }
}
