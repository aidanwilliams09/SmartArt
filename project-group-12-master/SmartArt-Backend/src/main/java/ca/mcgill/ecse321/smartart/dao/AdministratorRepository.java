package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Administrator;

/** @author Group 12 Interface used as a template for a repository of administrators. */
@RepositoryRestResource(collectionResourceRel = "administrator_data", path = "administrator_data")
public interface AdministratorRepository extends CrudRepository<Administrator, String> {

  Administrator findAdministratorByEmail(String email);
}
