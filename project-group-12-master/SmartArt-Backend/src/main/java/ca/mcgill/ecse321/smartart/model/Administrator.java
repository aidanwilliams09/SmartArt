package ca.mcgill.ecse321.smartart.model;

import javax.persistence.ManyToOne;

import javax.persistence.Entity;

/**
 * @author Group 12 The Administrator class represents the administrators in our system. The
 *     administrator is the one in charge of a Gallery.
 */
@Entity
public class Administrator extends User {

  @ManyToOne(optional = false)
  private Gallery gallery;

  /** The default class. */
  public Administrator() {
    super();
  }

  /**
   * Gets the Gallery that the Administrator is in charge of.
   *
   * @return the Gallery that the Administrator is in charge of.
   */
  public Gallery getGallery() {
    return this.gallery;
  }

  /**
   * Sets the Gallery that the Administrator is in charge of.
   *
   * @param gallery: Gallery that the Administrator will be in charge of.
   */
  public void setGallery(Gallery gallery) {
    this.gallery = gallery;
  }
}
