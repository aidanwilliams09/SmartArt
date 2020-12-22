package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Purchase;

/** @author Group 12 Interface used as a template for a repository of purchases. */
@RepositoryRestResource(collectionResourceRel = "purchase_data", path = "purchase_data")
public interface PurchaseRepository extends CrudRepository<Purchase, Integer> {

  Purchase findPurchaseByPurchaseID(int purchaseID);
}
