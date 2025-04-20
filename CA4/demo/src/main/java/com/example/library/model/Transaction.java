package com.example.library.model;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private ArrayList<Order> orders;
    private int numberOfBooks;
    private double totalPrice;
    String time;

    public Transaction(ArrayList<Order> orders, double totalPrice){
        this.orders = new ArrayList<>(orders);
        this.numberOfBooks = orders.size();
        this.totalPrice = totalPrice;
        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.time = purchaseTime.format(formatter);
    }

    public ArrayList<Order> getOrders(){
        return this.orders;
    }
    public String getTime() { return this.time; }
    public double getTotalPrice() { return  this.totalPrice; }

}
