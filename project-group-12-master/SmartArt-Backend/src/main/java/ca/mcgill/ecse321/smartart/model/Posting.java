package ca.mcgill.ecse321.smartart.model;

import java.sql.Date;
import javax.persistence.*;

/**
 * @author Group 12 This class represents a posting of an art piece that can be put on display in a
 *     Gallery for sale.
 */
@Entity
public class Posting {

  @Id
  @Column(name = "postingID")
  private int postingID;

  @ManyToOne(optional = false)
  private Artist artist;

  @ManyToOne(optional = false)
  private Gallery gallery;

  @Column(name = "price")
  private int price;

  @Column(name = "xDim")
  private double xDim;

  @Column(name = "yDim")
  private double yDim;

  @Column(name = "zDim")
  private double zDim;

  @Column(name = "title")
  private String title;

  @Column(name = "description")
  private String description;

  @Column(name = "artStatus")
  private ArtStatus artStatus;

  @Column(name = "date")
  private Date date;

  @Column(name = "image")
  private String image;

  /**
   * Sets the ArtStatus of the Posting.
   *
   * @param value: the new ArtStatus of the Posting.
   */
  public void setArtStatus(ArtStatus value) {
    this.artStatus = value;
  }

  /**
   * Gets the ArtStatus of the Posting.
   *
   * @return the ArtStatus of the posting.
   */
  public ArtStatus getArtStatus() {
    return this.artStatus;
  }

  /**
   * Sets the Posting ID of the Posting.
   *
   * @param value: the new Posting ID of the Posting.
   */
  public void setPostingID(int value) {
    this.postingID = value;
  }

  /**
   * Gets the Posting ID of the Posting.
   *
   * @return the Posting ID of the Posting.
   */
  public int getPostingID() {
    return this.postingID;
  }

  /**
   * Gets the Artist of the Posting.
   *
   * @return the Artist of the Posting.
   */
  public Artist getArtist() {
    return this.artist;
  }

  /**
   * Sets the Artist of the Posting.
   *
   * @param artist: the new Artist of the Posting.
   */
  public void setArtist(Artist artist) {
    this.artist = artist;
  }

  /**
   * Sets the title of the Posting.
   *
   * @param value: the new title of the Posting.
   */
  public void setTitle(String value) {
    this.title = value;
  }

  /**
   * Gets the title of the Posting.
   *
   * @return the title of the Posting.
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the price of the Posting.
   *
   * @param value: the new price of the Posting.
   */
  public void setPrice(int value) {
    this.price = value;
  }

  /**
   * Gets the price of the Posting.
   *
   * @return the price of the Posting.
   */
  public int getPrice() {
    return this.price;
  }

  /**
   * Sets the description of the Posting.
   *
   * @param value: the new description of the Posting.
   */
  public void setDescription(String value) {
    this.description = value;
  }

  /**
   * Gets the description of the Posting.
   *
   * @return the description of the Posting.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the X dimension of the Posting.
   *
   * @param value: the X dimension of the Posting.
   */
  public void setXDim(double value) {
    this.xDim = value;
  }

  /**
   * Gets the X dimension of the Posting.
   *
   * @return the X dimension of the Posting.
   */
  public double getXDim() {
    return this.xDim;
  }

  /**
   * Sets the Y dimension of the Posting.
   *
   * @param value: the Y dimension of the Posting.
   */
  public void setYDim(double value) {
    this.yDim = value;
  }

  /**
   * Gets the Y dimension of the Posting.
   *
   * @return the Y dimension of the Posting.
   */
  public double getYDim() {
    return this.yDim;
  }

  /**
   * Sets the Z dimension of the Posting.
   *
   * @param value: the Z dimension of the Posting.
   */
  public void setZDim(double value) {
    this.zDim = value;
  }

  /**
   * Gets the Z dimension of the Posting.
   *
   * @return the Z dimension of the Posting.
   */
  public double getZDim() {
    return this.zDim;
  }

  /**
   * Sets the Gallery of the Posting.
   *
   * @param gallery: the Gallery of the Posting.
   */
  public void setGallery(Gallery gallery) {
    this.gallery = gallery;
  }

  /**
   * Gets the Gallery of the Posting.
   *
   * @return the Gallery of the Posting.
   */
  public Gallery getGallery() {
    return this.gallery;
  }

  /**
   * Sets the date of the Posting.
   *
   * @param date: the new date of the Posting.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Gets the date of the Posting.
   *
   * @return the date of the Posting.
   */
  public Date getDate() {
    return this.date;
  }

  /**
   * Sets the image URL of the Posting.
   *
   * @param image: the new image URL of the Posting.
   */
  public void setImage(String image) {
    this.image = image;
  }

  /**
   * Gets the image URL of the Posting.
   *
   * @return the image URL of the Posting.
   */
  public String getImage() {
    return this.image;
  }
}
