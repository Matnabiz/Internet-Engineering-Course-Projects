package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;
    private Address address;
    private String role;
    private int balance;
    private ArrayList<Order> shoppingCart;
    private ArrayList<Order> borrowedBooks;
    private ArrayList<Order> boughtBooks;
    private double payableAmount;
    private ArrayList<List<Object>> transactionHistory; // every arrayList is a history , start with books
                                                            // and three last elements are number of books and total price and time
                                                            // { books , numberOfBooks , totalPrice , Time }

    public User(String username, String password, String email, Address address, String role, int balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.role = role;
        this.balance = balance;
        this.shoppingCart = new ArrayList<>();
        this.transactionHistory = new ArrayList<List<Object>>();
        this.borrowedBooks = new ArrayList<>();
        this.boughtBooks = new ArrayList<>();
    }

    public String getUsername() { return this.username; }

    public String getPassword() { return this.password; }

    public String getEmail() { return this.email; }

    public Address getAddress() { return this.address; }

    public String getRole() { return this.role; }

    public int getBalance() { return this.balance; }

    public double getPayableAmount() { return this.payableAmount; }

    public ArrayList<List<Object>> getTransactionHistory() { return this.transactionHistory; }

    public ArrayList<Order> getShoppingCart() { return this.shoppingCart; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(Address address) { this.address = address; }

    public void setRole(String role) { this.role = role; }

    private Order findOrder(String bookTitle){
        for (Order order : this.shoppingCart) {
            if (order.getBook().getTitle().equals(bookTitle)) {
                return order;
            }
        }
        return null;
    }

    public void addTransactionHistory(List<Object> newTransaction) {this.transactionHistory.add(newTransaction);}

    public void increaseBalance(int balanceToBeAdded) { this.balance += balanceToBeAdded; }

    public void setShoppingCart(ArrayList<Order> shoppingCart) { this.shoppingCart = shoppingCart; }

    public void addOrderToCart(Order orderToBeAddedToCart) {
        this.shoppingCart.add(orderToBeAddedToCart);
        if(orderToBeAddedToCart.getType().equals("buy"))
            this.increasePayableAmount(orderToBeAddedToCart.getBook().getPrice());
        else if(orderToBeAddedToCart.getType().equals("borrow"))
            this.increasePayableAmount(orderToBeAddedToCart.getBook().getPrice() * orderToBeAddedToCart.getBorrowDurationDays() / 10);
    }

    public void removeOrderFromCart(Book bookToBeRemovedFromCart) {
        Order orderToBeRemovedFromCart = findOrder(bookToBeRemovedFromCart.getTitle());
        this.shoppingCart.remove(orderToBeRemovedFromCart);
        if(orderToBeRemovedFromCart.getType() == "buy")
            this.decreasePayableAmount(orderToBeRemovedFromCart.getBook().getPrice());
        else if(orderToBeRemovedFromCart.getType() == "borrow")
            this.decreasePayableAmount(orderToBeRemovedFromCart.getBook().getPrice() * orderToBeRemovedFromCart.getBorrowDurationDays() / 10);
    }

    private void increasePayableAmount(double amount) { this.payableAmount += amount;}

    private void decreasePayableAmount(double amount) { this.payableAmount -= amount;}

    private void addPurchasedOrdersToShelf() {
        for (Order order : this.shoppingCart) {
            if ("buy".equals(order.getType())) {
                this.boughtBooks.add(order);
            } else if ("borrow".equals(order.getType())) {
                this.borrowedBooks.add(order);
            }
        }
    }

    private void makeTransactionHistory(){
        List<Object> transaction = new ArrayList<>();
        transaction.addAll(this.getShoppingCart());
        transaction.add(this.getShoppingCart().size());
        transaction.add(this.getPayableAmount());
        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = purchaseTime.format(formatter);
        transaction.add(formattedTime);
        this.addTransactionHistory(transaction);
    }

    public void updateInfoAfterCheckout() {
        this.makeTransactionHistory();
        this.addPurchasedOrdersToShelf();
        this.shoppingCart.clear();
        this.balance -= this.getPayableAmount();
        this.payableAmount = 0;
    }

    public boolean userHasBoughtBook(String bookTitle) {
        for (Order order : this.boughtBooks) {
            if (order.getBook().getTitle().equals(bookTitle))
                return true;
        }
        return false;
    }

    public boolean userHasBorrowedBook(String bookTitle) {
        for (Order order : this.borrowedBooks) {
            //Check if the borrowing time has expired
            if (order.getBook().getTitle().equals(bookTitle))
                return true;
        }
        return false;
    }



}
