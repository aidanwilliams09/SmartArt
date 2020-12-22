package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Artist;

/** @author Group 12 Interface used as a template for a repository of artists. */
@RepositoryRestResource(collectionResourceRel = "artist_data", path = "artist_data")
public interface ArtistRepository extends CrudRepository<Artist, String> {

  Artist findArtistByEmail(String email);
}
