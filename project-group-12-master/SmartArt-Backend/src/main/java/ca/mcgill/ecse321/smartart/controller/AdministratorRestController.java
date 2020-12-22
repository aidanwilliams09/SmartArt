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

import ca.mcgill.ecse321.smartart.dto.AdministratorDto;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.LoginDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.service.AdministratorService;

/** Writes administrator data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class AdministratorRestController {
	@Autowired private AdministratorService adminService;
	@Autowired private RestControllerHelper controllerHelper;

	/**
	 * Gets the list of all Administrators after HTTP request and puts into the HTTP response as JSON
	 * or XML.
	 *
	 * @return the list of Administrators as Dto.
	 */
	@GetMapping(value = {"/administrators", "/administrators/"})
	public ResponseEntity<?> getAllAdministrators() {
		return new ResponseEntity<>(
				adminService.getAllAdministrators().stream()
				.map(a -> controllerHelper.convertToDto(a))
				.collect(Collectors.toList()),
				HttpStatus.OK);
	}

	/**
	 * Gets an Administrator by email after HTTP request and puts into the HTTP response as JSON or
	 * XML.
	 *
	 * @param email: email of the Administrator.
	 * @return the email of the Administrator as Dto.
	 * @throws IllegalArgumentException
	 */
	@GetMapping(value = {"/administrators/{email}", "/administrators/{email}/"})
	public ResponseEntity<?> getAdministratorByEmail(@PathVariable("email") String email)
			throws IllegalArgumentException {
		try {
			Administrator administrator = adminService.getAdministrator(email);
			return new ResponseEntity<>(controllerHelper.convertToDto(administrator), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Creates an Administrator using Dto data after HTTP request and puts into the HTTP response as
	 * JSON or XML.
	 *
	 * @param data: the AdministratorDto data.
	 * @return the created Administrator as Dto.
	 * @throws IllegalArgumentException
	 */
	@PostMapping(value = {"/administrator/create", "/administrator/create/"})
	public ResponseEntity<?> createAdministrator(@RequestBody AdministratorDto data)
			throws IllegalArgumentException {
		try {
			Administrator administrator = adminService.createAdministrator(data);
			return new ResponseEntity<>(controllerHelper.convertToDto(administrator), HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = {"/administrator/login", "/administrator/login/"})
	public ResponseEntity<?> login(@RequestBody LoginDto login) throws IllegalArgumentException {
		try {
			Administrator administrator = adminService.login(login);
			return new ResponseEntity<>(controllerHelper.convertToDto(administrator), HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value = {"/administrator/update", "/administrator/update/"})
	  public ResponseEntity<?> updatePassword(@RequestBody AdministratorDto data) {
		  try {
			  Administrator administrator = adminService.updatePassword(data);
			  return new ResponseEntity<>(controllerHelper.convertToDto(administrator), HttpStatus.OK);
		  } catch (IllegalArgumentException e) {
			  return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
		  }
	  }
}
