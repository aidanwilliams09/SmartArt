package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Posting;

/** @author Group 12 Interface used as a template for a repository of postings. */
@RepositoryRestResource(collectionResourceRel = "posting_data", path = "posting_data")
public interface PostingRepository extends CrudRepository<Posting, Integer> {

  Posting findPostingByPostingID(int postingID);
}
