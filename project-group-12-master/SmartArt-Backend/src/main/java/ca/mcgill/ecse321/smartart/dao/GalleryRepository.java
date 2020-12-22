package ca.mcgill.ecse321.smartart.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ca.mcgill.ecse321.smartart.model.Gallery;

/** @author Group 12 Interface used as a template for a repository of galleries. */
@RepositoryRestResource(collectionResourceRel = "gallery_data", path = "gallery_data")
public interface GalleryRepository extends CrudRepository<Gallery, String> {

  Gallery findGalleryByName(String name);
}
