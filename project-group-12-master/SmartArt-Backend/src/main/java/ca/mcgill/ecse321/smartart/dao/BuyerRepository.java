package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Buyer;

/** @author Group 12 Interface used as a template for a repository of buyers. */
@RepositoryRestResource(collectionResourceRel = "buyer_data", path = "buyer_data")
public interface BuyerRepository extends CrudRepository<Buyer, String> {

  Buyer findBuyerByEmail(String email);
}
