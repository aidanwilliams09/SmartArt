package ca.mcgill.ecse321.smartart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.smartart.dao.AdministratorRepository;
import ca.mcgill.ecse321.smartart.dao.GalleryRepository;
import ca.mcgill.ecse321.smartart.dto.AdministratorDto;
import ca.mcgill.ecse321.smartart.dto.ArtistDto;
import ca.mcgill.ecse321.smartart.dto.LoginDto;
import ca.mcgill.ecse321.smartart.model.Administrator;
import ca.mcgill.ecse321.smartart.model.Artist;
import ca.mcgill.ecse321.smartart.model.Gallery;

@Service
public class AdministratorService {
  @Autowired private AdministratorRepository administratorRepository;
  @Autowired private GalleryRepository galleryRepository;
  @Autowired private ServiceHelper helper;

  /**
   * Creates an Administrator with an email, name, password and gallery. Then saves the
   * Administrator and Gallery to their respective repositories.
   *
   * @param email: the Administrator's email.
   * @param name: the Administrator's name.
   * @param password: the Administrator's password.
   * @param gallery: the Administrator's gallery.
   * @return returns the created Administrator.
   */
  @Transactional
  public Administrator createAdministrator(
      String email, String name, String password, Gallery gallery) {
    // Input validation
    String error = "";
    if (email == null || email.trim().length() == 0) {
      error = error + "Administrator email cannot be empty. ";
    }
    if (name == null || name.trim().length() == 0) {
      error = error + "Administrator name cannot be empty. ";
    }
    if (password == null || password.trim().length() == 0) {
      error = error + "Administrator password cannot be empty. ";
    }
    if (gallery == null) {
      error = error + "Administrator gallery cannot be empty. ";
    }
    if (administratorRepository.findAdministratorByEmail(email) != null) {
      error = error + "An Administrator with this email already exists";
    }

    error = error.trim();
    if (error.length() > 0) {
      throw new IllegalArgumentException(error);
    }

    Administrator administrator = new Administrator();
    administrator.setEmail(email);
    administrator.setName(name);
    administrator.setPassword(password);
    gallery.addAdministrator(administrator);
    administratorRepository.save(administrator);
    galleryRepository.save(gallery);
    return administrator;
  }

  /**
   * Creates an Administrator using Dto data.
   *
   * @param data: the data retrieved from AdministratorDto.
   * @return returns the created Administrator.
   */
  @Transactional
  public Administrator createAdministrator(AdministratorDto data) {
    Administrator admin =
        createAdministrator(
            data.getEmail(),
            data.getName(),
            data.getPassword(),
            helper.convertToModel(data.getGallery()));
    return admin;
  }

  /**
   * Gets the Administrator by its email attribute.
   *
   * @param email: the Administrator's email.
   * @return the retrieved Administrator.
   */
  @Transactional
  public Administrator getAdministrator(String email) {
    if (email == null || email.trim().length() == 0) {
      throw new IllegalArgumentException("Administrator email cannot be empty.");
    }
    Administrator administrator = administratorRepository.findAdministratorByEmail(email);
    return administrator;
  }

  /**
   * Gets a list of all the Administrators in the repository.
   *
   * @return the list of Administrators.
   */
  @Transactional
  public List<Administrator> getAllAdministrators() {
    return toList(administratorRepository.findAll());
  }
  
  @Transactional
  public Administrator login(LoginDto login) {
	  if(login.getEmail() == null || login.getPassword() == null) throw new IllegalArgumentException("Please fill in all required fields");
	  Administrator administrator = administratorRepository.findAdministratorByEmail(login.getEmail());
	  if(administrator == null) throw new IllegalArgumentException("There is no account associated with this email");
	  if(!login.getPassword().equals(administrator.getPassword())) throw new IllegalArgumentException("Invalid password");
	  return administrator;
	  
  }
  
  @Transactional
  public Administrator updatePassword(AdministratorDto data) {
	  if(data.getEmail() == null || data.getPassword() == null) throw new IllegalArgumentException("Please fill in all required fields");
	  Administrator administrator = administratorRepository.findAdministratorByEmail(data.getEmail());
	  if(administrator == null) throw new IllegalArgumentException("There is no account associated with this email");
	  administrator.setPassword(data.getPassword());
	  administratorRepository.save(administrator);
	  return administrator;
  }

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
