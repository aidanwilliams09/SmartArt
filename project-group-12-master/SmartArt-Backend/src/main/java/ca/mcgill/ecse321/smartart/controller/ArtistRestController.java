package ca.mcgill.ecse321.smartart.controller;

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

import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.LoginDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.service.ArtistService;

/** Writes Artist data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class ArtistRestController {
  @Autowired private ArtistService artistService;
  @Autowired private RestControllerHelper controllerHelper;

  /**
   * Gets a list of all the artists after HTTP request and puts into the HTTP response as JSON or
   * XML.
   *
   * @return the list of all the Artists as Dto.
   */
  @GetMapping(value = {"/artists", "/artists/"})
  public ResponseEntity<?> getAllArtists() {
    return new ResponseEntity<>(
        artistService.getAllArtists().stream()
            .map(a -> controllerHelper.convertToDto(a))
            .collect(Collectors.toList()),
        HttpStatus.OK);
  }

  /**
   * Gets an Artist by their email after HTTP request and puts into the HTTP response as JSON or
   * XMl.
   *
   * @param email: the Artist's email.
   * @return the retrieved Artist as Dto.
   */
  @GetMapping(value = {"/artists/{email}", "/artists/{email}/"})
  public ResponseEntity<?> getArtistByEmail(@PathVariable("email") String email) {
    try {
    	Artist artist = artistService.getArtist(email);
    	return new ResponseEntity<>(controllerHelper.convertToDto(artist), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
    	return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Creates an Artist from Dto data after HTTP request and puts into the HTTP response as JSON or
   * XML.
   *
   * @param data: the data from ArtistDto.
   * @return the created Artist as Dto.
   */
  @PostMapping(value = {"/artist/create", "/artist/create/"})
  public ResponseEntity<?> createArtist(@RequestBody ArtistDto data) {
	  try {
		  Artist artist = artistService.createArtist(data);
		  return new ResponseEntity<>(controllerHelper.convertToDto(artist), HttpStatus.CREATED);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }

  @PostMapping(value = {"/artist/login", "/artist/login/"})
  public ResponseEntity<?> login(@RequestBody LoginDto login) {
	  try {
		  Artist artist = artistService.login(login);
		  return new ResponseEntity<>(controllerHelper.convertToDto(artist), HttpStatus.OK);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }
  
  @PutMapping(value = {"/artist/update", "/artist/update/"})
  public ResponseEntity<?> updatePassword(@RequestBody ArtistDto data) {
	  try {
		  Artist artist = artistService.updatePassword(data);
		  return new ResponseEntity<>(controllerHelper.convertToDto(artist), HttpStatus.OK);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }
}
