package org.example;

import java.util.Date;

public class BookingWithCustomer {
    private int id;
    private Customer customer;
    private Date checkIn;
    private Date checkOut;

    public BookingWithCustomer(int id, Customer customer, Date checkIn, Date checkOut) {
        this.id = id;
        this.customer = customer;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }
}
