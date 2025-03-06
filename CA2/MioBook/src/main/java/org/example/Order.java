package org.example;

public class Order {
    private Book book;
    private String type; // "purchase" or "borrow"
    private int borrowDurationDays;

    public Order(Book book, String type, int borrowDurationDays) {
        this.book = book;
        this.type = type;
        this.borrowDurationDays = borrowDurationDays;
    }

    public Book getBook() { return this.book; }
    public String getType() { return this.type; }
    public int getBorrowDurationDays() { return this.borrowDurationDays; }
    public void setBook(Book book) { this.book = book; }
    public int getOrderPrice(){if(this.type.equals("buy")){return book.getPrice();}
                                else{return (int) Math.floor(book.getPrice()*this.borrowDurationDays/10);}
    }
}
