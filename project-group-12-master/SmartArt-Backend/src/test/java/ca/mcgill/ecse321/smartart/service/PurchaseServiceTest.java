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

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.smartart.model.*;
import ca.mcgill.ecse321.smartart.dao.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseServiceTest {

  @Mock private PurchaseRepository purchaseDao;
  @Mock private BuyerRepository buyerDao;

  @InjectMocks private PurchaseService purchaseService;
  @InjectMocks private BuyerService buyerService;

  private static final int PURCHASE_KEY = 200;
  private static final int NONEXISTING_PURCHASE = -1;
  private static final String BUYER_KEY = "TestBuyer";

  @BeforeEach
  public void setMockOutput() {

    lenient()
        .when(purchaseDao.findPurchaseByPurchaseID(anyInt()))
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(PURCHASE_KEY)) {
                Purchase purchase = new Purchase();
                purchase.setPurchaseID(PURCHASE_KEY);
                return purchase;
              } else {
                return null;
              }
            });
    lenient()
        .when(purchaseDao.findAll())
        .thenAnswer(
            (InvocationOnMock invocation) -> {
              List<Purchase> listpurchases = new ArrayList<Purchase>();
              listpurchases.add(purchaseDao.findPurchaseByPurchaseID(PURCHASE_KEY));
              return listpurchases;
            });
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

    // Whenever anything is saved, just return the parameter object
    Answer<?> returnParameterAsAnswer =
        (InvocationOnMock invocation) -> {
          return invocation.getArgument(0);
        };
    lenient().when(purchaseDao.save(any(Purchase.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(buyerDao.save(any(Buyer.class))).thenAnswer(returnParameterAsAnswer);
  }

  ////////////////////////////
  ////// Purchase tests////////
  ////////////////////////////

  @Test
  public void testCreatePurchase() {
    int purchaseID = 982423;
    Buyer buyer = new Buyer();
    Purchase purchase = null;

    try {
      purchase = purchaseService.createPurchase(purchaseID, buyer);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }
    assertNotNull(purchase);
    assertEquals(purchaseID, purchase.getPurchaseID());
  }

  @Test
  public void testCreatePurchaseNull() {

    int purchaseID = 984532;
    Buyer buyer = null;
    Purchase purchase = null;
    String error = null;

    try {
      purchase = purchaseService.createPurchase(purchaseID, buyer);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(purchase);
    assertEquals(error, "Purchase buyer cannot be empty.");
  }

  @Test
  public void testGetExistingPurchase() {
    assertEquals(PURCHASE_KEY, purchaseService.getPurchase(PURCHASE_KEY).getPurchaseID());
  }

  @Test
  public void testGetNonExistingPurchase() {
    assertNull(purchaseService.getPurchase(NONEXISTING_PURCHASE));
  }

  @Test
  public void testGetAllPurchases() {
    assertEquals(PURCHASE_KEY, purchaseService.getAllPurchases().get(0).getPurchaseID());
  }
}
