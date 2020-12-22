package ca.mcgill.ecse321.smartart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.service.GalleryService;

/** Writes Gallery data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class GalleryRestController {
  @Autowired private GalleryService galleryService;
  @Autowired private RestControllerHelper controllerHelper;

  /**
   * Gets a list of all the Galleries after HTTP request and puts into the HTTP response as JSON or
   * XML.
   *
   * @return the list of all Galleries as Dto.
   */
  @GetMapping(value = {"/galleries", "/galleries/"})
  public List<GalleryDto> getAllGalleries() {
    return galleryService.getAllGalleries().stream()
        .map(g -> controllerHelper.convertToDto(g))
        .collect(Collectors.toList());
  }

  /**
   * Gets a Gallery by its name after an HTTP request and puts into HTTP response as JSON or XML.
   *
   * @param name: the name of the Gallery.
   * @return the created Gallery as Dto.
   * @throws IllegalArgumentException
   */
  @GetMapping(value = {"/galleries/{name}", "/galleries/{name}/"})
  public ResponseEntity<?> getGalleryByName(@PathVariable("name") String name)
      throws IllegalArgumentException {
    try {
      Gallery gallery = galleryService.getGallery(name);
      return new ResponseEntity<>(controllerHelper.convertToDto(gallery), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Creates a Gallery using Dto data after an HTTP request and puts into HTTP response as JSON or
   * XML.
   *
   * @param data: the GalleryDto data.
   * @return the created Gallery as Dto.
   */
  @PostMapping(value = {"/gallery/create", "/gallery/create/"})
  public ResponseEntity<?> createGallery(@RequestBody GalleryDto data) {
    try {
      Gallery gallery = galleryService.createGallery(data);
      return new ResponseEntity<>(controllerHelper.convertToDto(gallery), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }
  
  @PutMapping(value = {"/gallery/update", "/gallery/update/"})
  public ResponseEntity<?> updateCommission(@RequestBody GalleryDto data) {
	    try {
	      Gallery gallery = galleryService.updateCommission(data);
	      return new ResponseEntity<>(controllerHelper.convertToDto(gallery), HttpStatus.CREATED);
	    } catch (IllegalArgumentException e) {
	      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	    }
	  }
}
