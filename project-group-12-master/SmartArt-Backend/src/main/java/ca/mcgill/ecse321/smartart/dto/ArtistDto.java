package ca.mcgill.ecse321.smartart.dto;

public class ArtistDto {

  private String email;
  private String name;
  private String password;
  private GalleryDto gallery;

  public ArtistDto() {}

  public ArtistDto(String email) {
    this(email, "user", "abc123", new GalleryDto());
  }

  public ArtistDto(String email, GalleryDto gallery) {
    this(email, "user", "abc123", gallery);
  }

  public ArtistDto(String email, String name, String password, GalleryDto gallery) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.gallery = gallery;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public GalleryDto getGallery() {
    return this.gallery;
  }

  public void setGallery(GalleryDto gallery) {
    this.gallery = gallery;
  }
}
