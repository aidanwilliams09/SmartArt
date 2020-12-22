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
public class BuyerServiceTest {

  @Mock private BuyerRepository buyerDao;
  @Mock private GalleryRepository galleryDao;

  @InjectMocks private BuyerService buyerService;
  @InjectMocks private GalleryService galleryService;

  private static final String BUYER_KEY = "TestBuyer";
  private static final String NONEXISTING_BUYER = "NotABuyer";
  private static final String GALLERY_KEY = "TestGallery";

  @BeforeEach
  public void setMockOutput() {
    lenient()
        .when(buyerDao.findBuyerByEmail(anyString()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(BUYER_KEY)) {
                Buyer buyer = new Buyer();
                buyer.setEmail(BUYER_KEY);
                buyerDao.save(buyer);
                return buyer;
              } else {
                return null;
              }
            });
    lenient()
        .when(buyerDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Buyer> listBuyers = new ArrayList<Buyer>();
              listBuyers.add(buyerDao.findBuyerByEmail(BUYER_KEY));
              return listBuyers;
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
    lenient().when(buyerDao.save(any(Buyer.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(galleryDao.save(any(Gallery.class))).thenAnswer(returnParameterAsAnswer);
  }

  ////////////////////////////
  ////// Buyer tests///////////
  ////////////////////////////

  @Test
  public void testCreateBuyer() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    Gallery gallery = new Gallery();
    Buyer buyer = null;
    try {
      buyer = buyerService.createBuyer(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(buyer);
    assertEquals(name, buyer.getName());
  }

  @Test
  public void testCreateBuyerNull() {
    String email = "bob@mail.com";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = null;
    Buyer buyer = null;
    try {
      buyer = buyerService.createBuyer(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(buyer);
    // check error
    assertEquals("Buyer gallery cannot be empty.", error);
  }

  @Test
  public void testCreateBuyerEmpty() {
    String email = "";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Buyer buyer = null;
    try {
      buyer = buyerService.createBuyer(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(buyer);
    // check error
    assertEquals("Buyer email cannot be empty.", error);
  }

  @Test
  public void testCreateBuyerSpaces() {
    String email = "  ";
    String name = "bob";
    String password = "123";
    String error = null;
    Gallery gallery = new Gallery();
    Buyer buyer = null;
    try {
      buyer = buyerService.createBuyer(email, name, password, gallery);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }

    assertNull(buyer);
    // check error
    assertEquals("Buyer email cannot be empty.", error);
  }

  @Test
  public void testGetExistingBuyer() {
    assertEquals(BUYER_KEY, buyerService.getBuyer(BUYER_KEY).getEmail());
  }

  @Test
  public void testGetNonExistingBuyer() {
    assertNull(buyerService.getBuyer(NONEXISTING_BUYER));
  }

  @Test
  public void testGetAllBuyers() {
    assertEquals(BUYER_KEY, buyerService.getAllBuyers().get(0).getEmail());
  }
}
