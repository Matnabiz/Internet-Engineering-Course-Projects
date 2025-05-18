package com.example.library.model;

import com.example.library.entity.BookEntity;

public class Order {
    private BookEntity book;
    private String type; // "purchase" or "borrow"
    private int borrowDurationDays;

    public Order(BookEntity book, String type, int borrowDurationDays) {
        this.book = book;
        this.type = type;
        this.borrowDurationDays = borrowDurationDays;
    }

    public BookEntity getBook() { return this.book; }
    public String getType() { return this.type; }
    public int getBorrowDurationDays() { return this.borrowDurationDays; }
    public void setBook(BookEntity book) { this.book = book; }
    public int getOrderPrice(){
        if(this.type.equals("buy")){return book.getPrice();}
        else{return (book.getPrice() * this.borrowDurationDays) /10;}
    }
}
