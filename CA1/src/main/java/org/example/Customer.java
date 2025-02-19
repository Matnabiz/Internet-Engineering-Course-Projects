package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

    private String ssn;
    private String name;
    private String phoneNumber;
    private int age;

    @JsonCreator
    public Customer(@JsonProperty("ssn") String ssn, @JsonProperty("phone") String phoneNumber, @JsonProperty("age") int age, @JsonProperty("name") String name) {
        this.ssn = ssn;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.name = name;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getPhone() { return this.phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void displayInfo() {
        System.out.println("Name: " + this.name);
        System.out.println("Age: " + this.age);
        System.out.println("National Number: " + this.ssn);
        System.out.println("Phone Number: " + this.phoneNumber);
    }
}
