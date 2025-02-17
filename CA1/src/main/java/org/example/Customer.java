package org.example;

public class Customer {

    private String ssn;
    private String phoneNumber;
    private int age;
    private String name;

    public Customer(String ssn, String phoneNumber, int age, String name) {
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
        System.out.println("National Number: " + ssn);
        System.out.println("Phone Number: " + phoneNumber);
    }
}
