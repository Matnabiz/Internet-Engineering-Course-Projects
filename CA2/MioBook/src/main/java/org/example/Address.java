package org.example;

public class Address<Country, City> {
    public final Country userCountry;
    public final City userCity;
    public Address(Country country, City city) {
        this.userCountry = country;
        this.userCity = city;
    }
}
