package ca.mcgill.ecse321.smartart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.smartart.dao.BuyerRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.BuyerDto;
import ca.mcgill.ecse321.smartart.dto.LoginDto;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Buyer;
import ca.mcgill.ecse321.smartart.model.Gallery;

@Service
public class BuyerService {
  @Autowired private BuyerRepository buyerRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private ServiceHelper helper;

  /**
   * Creates a Buyer with an email, name, password and gallery. Then saves the Buyer and Gallery to
   * their respective repositories.
   *
   * @param email: the Buyer's email.
   * @param name: the Buyer's name.
   * @param password: the Buyer's password.
   * @param gallery: the Buyer's gallery.
   * @return returns the created Buyer.
   */
  @Transactional
  public Buyer createBuyer(String email, String name, String password, Gallery gallery) {
    // Input validation
    String error = "";
    if (email == null || email.trim().length() == 0) {
      error = error + "Buyer email cannot be empty. ";
    }
    if (name == null || name.trim().length() == 0) {
      error = error + "Buyer name cannot be empty. ";
    }
    if (password == null || password.trim().length() == 0) {
      error = error + "Buyer password cannot be empty. ";
    }
    if (gallery == null) {
      error = error + "Buyer gallery cannot be empty. ";
    }
    if (buyerRepository.findBuyerByEmail(email) != null) {
      error = error + "A Buyer with this email already exists";
    }

    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }

    Buyer buyer = new Buyer();
    buyer.setEmail(email);
    buyer.setName(name);
    buyer.setPassword(password);
    gallery.addBuyer(buyer);
    buyerRepository.save(buyer);
    galleryRepository.save(gallery);
    return buyer;
  }

  /**
   * Creates a Buyer using Dto data.
   *
   * @param data: the data from BuyerDto.
   * @return the created Buyer.
   */
  @Transactional
  public Buyer createBuyer(BuyerDto data) {
    Buyer buyer =
        createBuyer(
            data.getEmail(),
            data.getName(),
            data.getPassword(),
            helper.convertToModel(data.getGallery()));
    return buyer;
  }

  /**
   * Gets a Buyer by its email attribute.
   *
   * @param email: the Buyer's email.
   * @return the retrieved Buyer.
   */
  @Transactional
  public Buyer getBuyer(String email) {
    if (email == null || email.trim().length() == 0) {
      throw new IllegalArgumentException("Buyer email cannot be empty.");
    }

    Buyer buyer = buyerRepository.findBuyerByEmail(email);
    return buyer;
  }

  /**
   * Gets a list of all the Buyers in the buyer repository.
   *
   * @return the list of Buyers.
   */
  @Transactional
  public List<Buyer> getAllBuyers() {
    return toList(buyerRepository.findAll());
  }
  
  @Transactional
  public Buyer login(String email, String password) {
	  Buyer buyer = buyerRepository.findBuyerByEmail(email);
	  if(buyer == null) throw new IllegalArgumentException("There is no account associated with this email");
	  if(!password.equals(buyer.getPassword())) throw new IllegalArgumentException("Invalid password");
	  return buyer;
  }
  
  @Transactional
  public Buyer login(LoginDto login) {
	  Buyer buyer = buyerRepository.findBuyerByEmail(login.getEmail());
	  if(buyer == null) throw new IllegalArgumentException("There is no account associated with this email");
	  if(!login.getPassword().equals(buyer.getPassword())) throw new IllegalArgumentException("Invalid password");
	  return buyer;
  }
  
  @Transactional
  public Buyer updatePassword(BuyerDto data) {
	  if(data.getEmail() == null || data.getPassword() == null) throw new IllegalArgumentException("Please fill in all required fields");
	  Buyer buyer = buyerRepository.findBuyerByEmail(data.getEmail());
	  if(buyer == null) throw new IllegalArgumentException("There is no account associated with this email");
	  buyer.setPassword(data.getPassword());
	  buyerRepository.save(buyer);
	  return buyer;
  }

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
