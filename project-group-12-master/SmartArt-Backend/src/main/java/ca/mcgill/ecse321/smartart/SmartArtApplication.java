package ca.mcgill.ecse321.smartart;

import ca.mcgill.ecse321.smartart.controller.GalleryRestController;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.model.Gallery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/** @author Group 12 Main class that runs our Smart Art application. */
@RestController
@SpringBootApplication
public class SmartArtApplication {
  @Autowired private GalleryRepository galleryRepository;

  public static void main(String[] args) {
    SpringApplication.run(SmartArtApplication.class, args);
  }

  @RequestMapping("/")
  public void checkGallery() {
    Gallery gallery;
    gallery = galleryRepository.findGalleryByName("SmartArt");
    if(gallery == null) {
      gallery = new Gallery();
      gallery.setName("SmartArt");
      gallery.setCity("Montreal");
      gallery.setCommission(0.05);
      galleryRepository.save(gallery);
    }
  }
}
