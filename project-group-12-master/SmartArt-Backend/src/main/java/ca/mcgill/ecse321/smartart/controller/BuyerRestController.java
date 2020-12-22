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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.LoginDto;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.service.BuyerService;

/** Writes Buyer data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class BuyerRestController {
  @Autowired private BuyerService buyerService;
  @Autowired private RestControllerHelper controllerHelper;

  /**
   * Gets a list of all the Buyers after HTTP request and puts into the HTTP response as JSON or
   * XMl.
   *
   * @return the list of all the Buyers as Dto.
   */
  @GetMapping(value = {"/buyers", "/buyers/"})
  public ResponseEntity<?> getAllBuyers() {
    return new ResponseEntity<>(
        buyerService.getAllBuyers().stream()
            .map(b -> controllerHelper.convertToDto(b))
            .collect(Collectors.toList()),
        HttpStatus.OK);
  }

  /**
   * Gets a Buyer by his email after HTTP request and puts into the HTTP response as JSON or XML.
   *
   * @param email: the email of the Buyer.
   * @return the Buyer as Dto.
   * @throws IllegalArgumentException
   */
  @GetMapping(value = {"/buyers/{email}", "/buyers/{email}/"})
  public ResponseEntity<?> getBuyerByEmail(@PathVariable("email") String email)
      throws IllegalArgumentException {
    try {
      Buyer buyer = buyerService.getBuyer(email);
      return new ResponseEntity<>(controllerHelper.convertToDto(buyer), HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Creates a Buyer by their Dto data after HTTP request and puts into the HTTP response as JSON or
   * XMl.
   *
   * @param data: data from BuyerDto.
   * @return the Buyer as Dto.
   * @throws IllegalArgumentException
   */
  @PostMapping(value = {"/buyer/create", "/buyer/create/"})
  public ResponseEntity<?> createBuyer(@RequestBody BuyerDto data) throws IllegalArgumentException {
    try {
      Buyer buyer = buyerService.createBuyer(data);
      return new ResponseEntity<>(controllerHelper.convertToDto(buyer), HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }
  
  @GetMapping(value = {"/android/login", "/android/login/"})
  public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) throws IllegalArgumentException {
	  try {
		  Buyer buyer = buyerService.login(email, password);
		  return new ResponseEntity<>(controllerHelper.convertToDto(buyer), HttpStatus.OK);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }
  
  @PostMapping(value = {"/buyer/login", "/buyer/login/"})
  public ResponseEntity<?> login(@RequestBody LoginDto data) throws IllegalArgumentException {
	  try {
		  Buyer buyer = buyerService.login(data);
		  return new ResponseEntity<>(controllerHelper.convertToDto(buyer), HttpStatus.OK);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }
  
  @PutMapping(value = {"/buyer/update", "/buyer/update/"})
  public ResponseEntity<?> updatePassword(@RequestBody BuyerDto data) {
	  try {
		  Buyer buyer = buyerService.updatePassword(data);
		  return new ResponseEntity<>(controllerHelper.convertToDto(buyer), HttpStatus.OK);
	  } catch (IllegalArgumentException e) {
		  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
	  }
  }
}
