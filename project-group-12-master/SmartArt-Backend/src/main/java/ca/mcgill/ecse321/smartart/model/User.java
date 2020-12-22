package ca.mcgill.ecse321.smartart.model;

import javax.persistence.Column;
import javax.persistence.Id;

import javax.persistence.MappedSuperclass;

/**
 * @author Group 12 Abstract class User is used as a template for Buyer, Artist and Administrator.
 *     All general information for users is provided in this class.
 */
@MappedSuperclass
public abstract class User {
  @Id
  @Column(name = "email")
  private String email;

  @Column(name = "name")
  private String name;

  @Column(name = "password")
  private String password;

  /**
   * Sets the User's password.
   *
   * @param value: the User's password.
   */
  public void setPassword(String value) {
    this.password = value;
  }

  /**
   * Gets the User's password.
   *
   * @return the User's password.
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Sets the name of the User.
   *
   * @param value: the name of the User.
   */
  public void setName(String value) {
    this.name = value;
  }

  /**
   * Gets the name of the User.
   *
   * @return the name of the User.
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the email of the User.
   *
   * @param value: the email of the User.
   */
  public void setEmail(String value) {
    this.email = value;
  }

  /**
   * Gets the email of the User.
   *
   * @return the email of the User.
   */
  public String getEmail() {
    return this.email;
  }
}
