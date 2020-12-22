package ca.mcgill.ecse321.smartart.dto;

public class GalleryDto {

  private String name;
  private String city;
  private double commission;

  public GalleryDto() {}

  public GalleryDto(String name) {
    this(name, "mtl", 0.1);
  }

  public GalleryDto(String name, String city, double commission) {
    this.name = name;
    this.city = city;
    this.commission = commission;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public double getCommission() {
    return this.commission;
  }

  public void setCommission(double commission) {
    this.commission = commission;
  }
}
