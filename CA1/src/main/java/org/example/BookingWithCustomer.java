package org.example;

import java.util.Date;

public class BookingWithCustomer {
    private int id;
    private Customer customer;
    private Date checkInDate;
    private Date checkOutDate;

    public BookingWithCustomer(int id, Customer customer, Date checkInDate, Date checkOutDate) {
        this.id = id;
        this.customer = customer;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

    public Date getCheckInDate() { return checkInDate; }
    public void setCheckInDate(Date checkInDate) { this.checkInDate = checkInDate; }

    public Date getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(Date checkOutDate) { this.checkOutDate = checkOutDate; }
}
