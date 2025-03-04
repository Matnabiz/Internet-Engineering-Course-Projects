package org.example;

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
    private int payableAmount;
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

    public int getPayableAmount() { return this.payableAmount; }

    public ArrayList<List<Object>> getTransactionHistory() { return this.transactionHistory; }

    public ArrayList<Order> getShoppingCart() { return this.shoppingCart; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(Address address) { this.address = address; }

    public void setRole(String role) { this.role = role; }

    public void addTransactionHistory(List<Object> newTransaction) {this.transactionHistory.add(newTransaction);}

    public void increaseBalance(int balanceToBeAdded) { this.balance += balanceToBeAdded; }

    public void setShoppingCart(ArrayList<Order> shoppingCart) { this.shoppingCart = shoppingCart; }

    public void addOrderToCart(Order orderToBeAddedToCart) {
        this.shoppingCart.add(orderToBeAddedToCart);
        this.increasePayableAmount(orderToBeAddedToCart.book.getPrice());
    }

    public void deleteOrderFromCart(Order orderToBeDeletedFromCart) {
        this.shoppingCart.remove(orderToBeDeletedFromCart);
        decreasePayableAmount(orderToBeDeletedFromCart.book.getPrice());
    }

    private void increasePayableAmount(int amount) { this.payableAmount += amount;}

    private void decreasePayableAmount(int amount) { this.payableAmount -= amount;}

    private void addBoughtBooksToShelf(){ this.boughtBooks.addAll(this.shoppingCart); }

    public void updateInfoAfterCheckout() {
        this.addBoughtBooksToShelf();
        this.shoppingCart.clear();
        this.balance -= this.getPayableAmount();
        this.payableAmount = 0;
    }


}
