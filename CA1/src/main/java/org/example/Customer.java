package org.example;

public class Customer {

    private String nationalNumber;
    private String phoneNumber;
    private int age;
    private String name;

    public Customer(String nationalNumber, String phoneNumber, int age, String name) {
        this.nationalNumber = nationalNumber;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.name = name;
    }

    public String getNationalNumber() {
        return nationalNumber;
    }

    public void setNationalNumber(String nationalNumber) {
        this.nationalNumber = nationalNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setNumber(String number) {
        this.phoneNumber = number;
    }

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
        System.out.println("Name: " + name);
        System.out.println("Age: " + age);
        System.out.println("National Number: " + nationalNumber);
        System.out.println("Phone Number: " + phoneNumber);
    }
}
