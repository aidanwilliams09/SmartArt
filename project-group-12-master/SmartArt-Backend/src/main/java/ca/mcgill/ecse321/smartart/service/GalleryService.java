package ca.mcgill.ecse321.smartart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Gallery;

@Service
public class GalleryService {
  @Autowired private GalleryRepository galleryRepository;

  /**
   * Creates a gallery with a name, city and commission and then saves it to the gallery repository.
   *
   * @param name: the name of the Gallery.
   * @param city: the city of the Gallery.
   * @param commission: the commission demanded from the Gallery.
   * @return the created Gallery.
   */
  @Transactional
  public Gallery createGallery(String name, String city, double commission) {
    // Input validation
    String error = "";
    if (name == null || name.trim().length() == 0) {
      error = error + "Gallery name cannot be empty. ";
    }
    if (city == null || city.trim().length() == 0) {
      error = error + "Gallery city cannot be empty. ";
    }
    if (commission < 0) {
      error = error + "Gallery commission cannot be less than 0. ";
    }
    if (galleryRepository.findGalleryByName(name) != null) {
      error = error + "A Gallery by this name already exists";
    }

    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }

    Gallery gallery = new Gallery();
    gallery.setName(name);
    gallery.setCity(city);
    gallery.setCommission(commission);
    galleryRepository.save(gallery);
    return gallery;
  }

  /**
   * Creates a Gallery using Dto data.
   *
   * @param data: the data from GalleryDto.
   * @return the created Gallery.
   */
  @Transactional
  public Gallery createGallery(GalleryDto data) {
    Gallery gallery = createGallery(data.getName(), data.getCity(), data.getCommission());
    return gallery;
  }

  /**
   * Gets a gallery by its name attribute.
   *
   * @param name: the name of the Gallery.
   * @return the retrieved Gallery.
   */
  @Transactional
  public Gallery getGallery(String name) {
    if (name == null || name.trim().length() == 0) {
      throw new IllegalArgumentException("Gallery name cannot be empty.");
    }
    Gallery gallery = galleryRepository.findGalleryByName(name);
    return gallery;
  }

  /**
   * Gets a list of all the Galleries in the gallery repository.
   *
   * @return the list of Galleries.
   */
  @Transactional
  public List<Gallery> getAllGalleries() {
    return toList(galleryRepository.findAll());
  }
  
  @Transactional
  public Gallery updateCommission(GalleryDto data) {
	  if(data.getName() == null || data.getCommission() == 0) throw new IllegalArgumentException("Please fill in all required fields");
	  Gallery gallery = galleryRepository.findGalleryByName(data.getName());
	  if(gallery == null) throw new IllegalArgumentException("There is no gallery with this name");
	  gallery.setCommission(data.getCommission());
	  galleryRepository.save(gallery);
	  return gallery;
  }

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
