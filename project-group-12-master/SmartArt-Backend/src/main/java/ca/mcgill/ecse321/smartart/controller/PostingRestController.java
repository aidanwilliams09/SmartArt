package ca.mcgill.ecse321.smartart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.smartart.dto.PostingDto;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.service.PostingService;

/** Writes Posting data into the HTTP response as JSON or XML after an HTTP request. */
@CrossOrigin(origins = "*")
@RestController
public class PostingRestController {
  @Autowired private PostingService postingService;
  @Autowired private RestControllerHelper controllerHelper;

  /**
   * Gets a list of all Postings after an HTTP request and puts into HTTP request as JSON or XMl.
   *
   * @return the list of all Postings as Dto.
   */
  @GetMapping(value = {"/postings", "/postings/"})
  public ResponseEntity<?> getAllPostings() {
    List<PostingDto> postings =
        postingService.getAllPostings().stream()
            .map(p -> controllerHelper.convertToDto(p))
            .collect(Collectors.toList());
    return new ResponseEntity<>(postings, HttpStatus.OK);
  }

  /**
   * Gets a Posting by its posting ID after an HTTP request and puts into HTTP request as JSON or
   * XMl.
   *
   * @param postingID: the ID of the Posting.
   * @return the retrieved Posting as Dto.
   */
  @GetMapping(value = {"/postings/{postingID}", "/postings/{postingID}/"})
  public ResponseEntity<?> getPostingByPostingID(@PathVariable("postingID") int postingID) {
    PostingDto posting = controllerHelper.convertToDto(postingService.getPosting(postingID));
    if (posting != null) return new ResponseEntity<>(posting, HttpStatus.OK);
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Gets all Postings by their Artist after an HTTP request and puts into HTTP request as JSON or
   * XMl.
   *
   * @param email: the email of the Artist.
   * @return the retrieved list of Postings as Dto.
   */
  @GetMapping(value = {"/postings/artist/{email}", "/postings/artist/{email}/"})
  public ResponseEntity<?> getPostingsByArtist(@PathVariable("email") String email) {
    List<PostingDto> postings =
        postingService.getPostingsByArtist(email).stream()
            .map(p -> controllerHelper.convertToDto(p))
            .collect(Collectors.toList());
    if (postings != null) return new ResponseEntity<>(postings, HttpStatus.OK);
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Creates a Posting with Dto data after an HTTP request and puts into HTTP response as JSON or
   * XMl.
   *
   * @param data: the data from PostingDto.
   * @return the Posting as Dto.
   */
  @PostMapping(value = {"/posting/create", "/posting/create/"})
  public ResponseEntity<?> createPosting(@RequestBody PostingDto data) {
    try {
      Posting posting = postingService.createPosting(data);
      PostingDto postingData = controllerHelper.convertToDto(posting);
      return new ResponseEntity<>(postingData, HttpStatus.CREATED);

    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Creates a Posting by an Administrator's request in HTTP and puts into the HTTP response as JSON
   * or XMl.
   *
   * @param data: the data in PostingDto.
   * @param adminEmail: the Administrator's email.
   * @param artistName: The name of the Artist of the Posting.
   * @return the created Posting as Dto.
   */
  @PostMapping(
      value = {"/posting/admin/create/{email}/{name}", "/posting/admin/create/{email}/{name}/"})
  public ResponseEntity<?> adminCreatePosting(
      @RequestBody PostingDto data,
      @PathVariable("email") String adminEmail,
      @PathVariable("name") String artistName) {
    try {
      Posting posting = postingService.adminCreatePosting(adminEmail, artistName, data);
      PostingDto postingData = controllerHelper.convertToDto(posting);
      return new ResponseEntity<>(postingData, HttpStatus.CREATED);

    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Updates the Posting with Dto data after an HTTP request and puts into the HTTP response as JSON
   * or XML.
   *
   * @param data: the data from PostingDto.
   * @return the Posting as Dto.
   */
  @PutMapping(value = {"/posting/update", "/posting/update/"})
  public ResponseEntity<?> updatePosting(@RequestBody PostingDto data) {
    try {
      Posting posting = postingService.updatePosting(data);
      PostingDto postingData = controllerHelper.convertToDto(posting);
      return new ResponseEntity<>(postingData, HttpStatus.OK);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * Deletes a Posting after an HTTP request and puts into HTTP request in JSON or XML.
   *
   * @param posting: the Posting in Dto.
   * @return the status of the deletion.
   */
  @DeleteMapping(value = {"/posting/delete", "/posting/delete/"})
  public ResponseEntity<?> deletePosting(@RequestBody PostingDto posting) {
    try {
      postingService.deletePosting(posting);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
    }
  }
}
