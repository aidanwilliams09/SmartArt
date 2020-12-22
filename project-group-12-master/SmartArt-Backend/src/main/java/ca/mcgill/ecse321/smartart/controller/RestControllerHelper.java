package ca.mcgill.ecse321.smartart.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.smartart.dto.AdministratorDto;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.GalleryDto;
import ca.mcgill.ecse321.smartart.dto.PostingDto;
import ca.mcgill.ecse321.smartart.dto.PurchaseDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.Gallery;
import ca.mcgill.ecse321.smartart.model.Posting;
import ca.mcgill.ecse321.smartart.model.Purchase;
import ca.mcgill.ecse321.smartart.service.ServiceHelper;

/** Writes various helper data into the HTTP response as JSON or XML. */
@CrossOrigin(origins = "*")
@RestController
public class RestControllerHelper {

  @Autowired private ServiceHelper serviceHelper;

  /** Clears the database after HTTP request and puts into the HTTP response as JSON or XML. */
  // Clear database
  @PostMapping(value = {"/clearDatabase", "/clearDatabase/"})
  public void clearDatabase() {
    serviceHelper.clearDatabase();
  }

  /**
   * Converts a Gallery from DAO to DTO.
   *
   * @param g: the Gallery to be converted.
   * @return the GalleryDto.
   */
  GalleryDto convertToDto(Gallery g) {
    if (g == null) {
      throw new IllegalArgumentException("There is no such Gallery.");
    }
    GalleryDto galleryDto = new GalleryDto(g.getName(), g.getCity(), g.getCommission());
    return galleryDto;
  }

  /**
   * Converts an Artist from DAO to DTO.
   *
   * @param a: the Artist to be converted.
   * @return the ArtistDto.
   */
  ArtistDto convertToDto(Artist a) {
    if (a == null) {
      throw new IllegalArgumentException("There is no such Artist.");
    }
    ArtistDto artistDto =
        new ArtistDto(a.getEmail(), a.getName(), a.getPassword(), convertToDto(a.getGallery()));
    return artistDto;
  }

  /**
   * Converts an Administrator from DAO to DTO.
   *
   * @param a: the Administrator to be converted.
   * @return the AdministratorDto.
   */
  AdministratorDto convertToDto(Administrator a) {
    if (a == null) {
      throw new IllegalArgumentException("There is no such Administrator.");
    }
    AdministratorDto administratorDto =
        new AdministratorDto(
            a.getEmail(), a.getName(), a.getPassword(), convertToDto(a.getGallery()));
    return administratorDto;
  }

  /**
   * Converts a Buyer from DAO to DTO.
   *
   * @param b: the Buyer to be converted.
   * @return the BuyerDto.
   */
  BuyerDto convertToDto(Buyer b) {
    if (b == null) {
      throw new IllegalArgumentException("There is no such Buyer.");
    }
    BuyerDto buyerDto =
        new BuyerDto(b.getEmail(), b.getName(), b.getPassword(), convertToDto(b.getGallery()));
    return buyerDto;
  }

  /**
   * Converts a Posting from DAO to DTO.
   *
   * @param p: the Posting to be converted.
   * @return the PostingDto.
   */
  PostingDto convertToDto(Posting p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such Posting.");
    }
    PostingDto postingDto =
        new PostingDto(
            p.getPostingID(),
            convertToDto(p.getArtist()),
            convertToDto(p.getGallery()),
            p.getPrice(),
            p.getXDim(),
            p.getYDim(),
            p.getZDim(),
            p.getTitle(),
            p.getDescription(),
            p.getArtStatus(),
            p.getDate(),
            p.getImage());
    return postingDto;
  }

  /**
   * Converts a Purchase from DAO to DTO.
   *
   * @param p: the Purchase to be converted.
   * @return the PurchaseDto.
   */
  PurchaseDto convertToDto(Purchase p) {
    if (p == null) {
      throw new IllegalArgumentException("There is no such Purchase.");
    }
    List<PostingDto> postings = null;
    if (p.getPostings() != null)
      postings =
              p.getPostings().stream().map(post -> convertToDto(post)).collect(Collectors.toList());

    PurchaseDto purchaseDto =
            new PurchaseDto(
                    p.getPurchaseID(),
                    convertToDto(p.getBuyer()),
                    p.getTotalPrice(),
                    postings,
                    p.getDeliveryType(),
                    p.getTime());
    return purchaseDto;
  }
}
