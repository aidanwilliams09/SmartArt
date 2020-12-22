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
public class AdministratorServiceTest {
  @Mock private AdministratorRepository administratorDao;
  @Mock private GalleryRepository galleryDao;

  @InjectMocks private AdministratorService administratorService;
  @InjectMocks private GalleryService galleryService;

  private static final String ADMINISTRATOR_KEY = "TestAdministrator";
  private static final String NONEXISTING_ADMINISTRATOR = "NotAnAdministrator";
  private static final String GALLERY_KEY = "TestGallery";

  @BeforeEach
  public void setMockOutput() {
    lenient()
        .when(administratorDao.findAdministratorByEmail(anyString()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(ADMINISTRATOR_KEY)) {
                Administrator administrator = new Administrator();
                administrator.setEmail(ADMINISTRATOR_KEY);
                return administrator;
              } else {
                return null;
              }
            });
    lenient()
        .when(administratorDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Administrator> listAdministrators = new ArrayList<>();
              listAdministrators.add(administratorDao.findAdministratorByEmail(ADMINISTRATOR_KEY));
              return listAdministrators;
            });
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
    // Whenever anything is saved, just return the parameter object
    Answer<?> returnParameterAsAnswer =
        (InvocationOnMock invocation) -> {
          return invocation.getArgument(0);
        };
    lenient()
        .when(administratorDao.save(any(Administrator.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(galleryDao.save(any(Gallery.class))).thenAnswer(returnParameterAsAnswer);
  }

  @Test
  public void testCreateAdministrator() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    Gallery gallery = new Gallery();
    Administrator administrator = null;
    try {
      administrator = administratorService.createAdministrator(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(administrator);
    assertEquals(name, administrator.getName());
  }

  @Test
  public void testCreateAdministratortNull() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = null;
    Administrator administrator = null;
    try {
      administrator = administratorService.createAdministrator(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(administrator);
    // check error
    assertEquals("Administrator gallery cannot be empty.", error);
  }

  @Test
  public void testCreateAdministratorEmpty() {
    String email = "";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Administrator administrator = null;
    try {
      administrator = administratorService.createAdministrator(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(administrator);
    // check error
    assertEquals("Administrator email cannot be empty.", error);
  }

  @Test
  public void testCreateAdministratorSpaces() {
    String email = "  ";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Administrator administrator = null;
    try {
      administrator = administratorService.createAdministrator(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(administrator);
    // check error
    assertEquals("Administrator email cannot be empty.", error);
  }

  @Test
  public void testGetExistingAdministrator() {
    assertEquals(
        ADMINISTRATOR_KEY, administratorService.getAdministrator(ADMINISTRATOR_KEY).getEmail());
  }

  @Test
  public void testGetNonExistingAdministrator() {
    assertNull(administratorService.getAdministrator(NONEXISTING_ADMINISTRATOR));
  }

  @Test
  public void testGetAllAdministrators() {
    assertEquals(ADMINISTRATOR_KEY, administratorService.getAllAdministrators().get(0).getEmail());
  }
}
