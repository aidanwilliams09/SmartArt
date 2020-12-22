package ca.mcgill.ecse321.smartart.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.OneToMany;

/**
 * @author Group 12 The Gallery class represents the gallery in which art pieces are displayed in
 *     for sale.
 */
@Entity
public class Gallery {

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = CascadeType.ALL)
  private Set<Buyer> buyers;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = CascadeType.ALL)
  private Set<Administrator> administrators;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = CascadeType.ALL)
  private Set<Artist> artists;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "gallery", cascade = CascadeType.ALL)
  private Set<Posting> postings;

  @Id
  @Column(name = "name")
  private String name;

  @Column(name = "city")
  private String city;

  @Column(name = "commission")
  private double commission;

  /**
   * Gets the Buyers from this Gallery.
   *
   * @return the Buyers from this Gallery.
   */
  public Set<Buyer> getBuyers() {
    return this.buyers;
  }

  /**
   * Sets new Buyers to this Gallery.
   *
   * @param buyers: the Buyers to be set to this Gallery.
   */
  public void setBuyers(Set<Buyer> buyers) {
    this.buyers = buyers;
  }

  /**
   * Adds a Buyer to the Gallery.
   *
   * @param buyer: the Buyer to be added.
   */
  public void addBuyer(Buyer buyer) {
    if (this.buyers == null) this.buyers = new HashSet<Buyer>();
    this.buyers.add(buyer);
    buyer.setGallery(this);
  }

  /**
   * Removes a Buyer from the Gallery.
   *
   * @param buyer: the Buyer to be removed
   */
  public void removeBuyer(Buyer buyer) {
    this.buyers.remove(buyer);
    buyer.setGallery(null);
  }

  /**
   * Gets the set of Administrators of this Gallery.
   *
   * @return the set of Administrators of this Gallery.
   */
  public Set<Administrator> getAdministrators() {
    return this.administrators;
  }

  /**
   * Sets a new set of Administrators of this Gallery.
   *
   * @param administrators: the new set of Administrators.
   */
  public void setAdministrators(Set<Administrator> administrators) {
    this.administrators = administrators;
  }

  /**
   * Adds an Administrator to the set of Administrators of this Gallery.
   *
   * @param administrator: Administrator to add.
   */
  public void addAdministrator(Administrator administrator) {
    if (this.administrators == null) this.administrators = new HashSet<Administrator>();
    this.administrators.add(administrator);
    administrator.setGallery(this);
  }

  /**
   * Removes an Administrator from the set of Administrators of this Gallery
   *
   * @param administrator: the Administrator to be removed.
   */
  public void removeAdministrator(Administrator administrator) {
    this.administrators.remove(administrator);
    administrator.setGallery(null);
  }

  /**
   * Gets the set of Artists of this Gallery.
   *
   * @return the set of Artists of this Gallery.
   */
  public Set<Artist> getArtists() {
    return this.artists;
  }

  /**
   * Replaces the current set of Artists with a new set of Artists.
   *
   * @param artists: the new set of Artists.
   */
  public void setArtists(Set<Artist> artists) {
    this.artists = artists;
  }

  /**
   * Adds an Artist to the current set of Artists.
   *
   * @param artist: the Artist to add.
   */
  public void addArtist(Artist artist) {
    if (this.artists == null) this.artists = new HashSet<Artist>();
    this.artists.add(artist);
    artist.setGallery(this);
  }

  /**
   * Removes an Artist from this set of Artists.
   *
   * @param artist: the Artist to be removed.
   */
  public void removeArtist(Artist artist) {
    this.artists.remove(artist);
    artist.setGallery(null);
  }

  /**
   * Gets the set of Postings of this Gallery.
   *
   * @return the set of Postings of this Gallery.
   */
  public Set<Posting> getPostings() {
    return this.postings;
  }

  /**
   * Replaces the current set of Postings with a new set.
   *
   * @param postings: the new Set of Postings of this Gallery.
   */
  public void setPostings(Set<Posting> postings) {
    this.postings = postings;
  }

  /**
   * Adds a Posting to the set of Postings of this Gallery.
   *
   * @param posting: the Posting to be added.
   */
  public void addPosting(Posting posting) {
    if (this.postings == null) this.postings = new HashSet<Posting>();
    this.postings.add(posting);
    posting.setGallery(this);
  }

  /**
   * Removes a Posting from the set of postings.
   *
   * @param posting: the Posting to be removed.
   */
  public void removePosting(Posting posting) {
    this.postings.remove(posting);
    posting.setGallery(null);
  }

  /**
   * Sets the name of this Gallery
   *
   * @param value: the new name of this Gallery.
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the name of this Gallery.
   *
   * @return the name of this Gallery.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the city of this Gallery.
   *
   * @param value: the city of this Gallery.
   */
  public void setCity(String value) {
    this.city = value;
  }

  /**
   * Gets the city of this Gallery.
   *
   * @return the city of this Gallery.
   */
  public String getCity() {
    return this.city;
  }

  /**
   * Sets the commission of this Gallery.
   *
   * @param value: the commission of this Gallery.
   */
  public void setCommission(double value) {
    this.commission = value;
  }

  /**
   * Gets the name of this Gallery.
   *
   * @return the name of this Gallery.
   */
  public double getCommission() {
    return this.commission;
  }
}
