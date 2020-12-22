package ca.mcgill.ecse321.smartart.model;

import javax.persistence.ManyToOne;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.OneToMany;

/**
 * @author Group 12 The Artist class represents the artists in our system. Artists are the creators
 *     of paintings and those paintings are displayed in galleries for sale in the form of postings.
 */
@Entity
public class Artist extends User {

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "artist", cascade = CascadeType.ALL)
  private Set<Posting> postings;

  @ManyToOne(optional = false)
  private Gallery gallery;

  /** The default class. */
  public Artist() {
    super();
  }

  /**
   * Sets the Postings that belong to this Artist.
   *
   * @param value: the Set of Postings belonging to this Artist.
   */
  public void setPostings(Set<Posting> value) {
    this.postings = value;
  }

  /**
   * Gets the Postings that belong to this Artist.
   *
   * @return the Set of Postings belonging to this Artist.
   */
  public Set<Posting> getPostings() {
    return this.postings;
  }

  /**
   * Adds a Posting to the Artist.
   *
   * @param posting: the Posting to be added.
   */
  public void addPosting(Posting posting) {
    if (this.postings == null) this.postings = new HashSet<Posting>();
    this.postings.add(posting);
    posting.setArtist(this);
  }

  /**
   * Removes a Posting from the Artist.
   *
   * @param posting: the Posting to be removed.
   * @return posting: the Posting that was removed.
   */
  public Posting removePosting(Posting posting) {
    if (this.postings == null) return null;
    this.postings.remove(posting);
    posting.setArtist(null);
    return posting;
  }

  /**
   * Gets the gallery that presents the Artist's Postings.
   *
   * @return the gallery that presents the Artist's Postings.
   */
  public Gallery getGallery() {
    return this.gallery;
  }

  /**
   * Sets the gallery that presents the Artist's Postings.
   *
   * @param gallery: the Gallery that will present the Artist's Postings.
   */
  public void setGallery(Gallery gallery) {
    this.gallery = gallery;
  }
}
