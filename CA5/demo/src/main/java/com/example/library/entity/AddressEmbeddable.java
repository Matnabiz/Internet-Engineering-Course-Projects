package com.example.library.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class AddressEmbeddable {
    private String country;
    private String city;

    // Getters & Setters
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
}
