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
    private ArrayList<Book> shoppingCart;
    private int payableAmount;
    private ArrayList<List<Object>> transactionHistory; // everay arrayList is a history , start with books
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
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public Address getAddress() { return address; }

    public String getRole() { return role; }

    public int getBalance() { return balance; }

    public int getPayableAmount() {return payableAmount;}

    public ArrayList<List<Object>> getTransactionHistory() {return transactionHistory;}

    public ArrayList<Book> getShoppingCart() { return shoppingCart; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(Address address) { this.address = address; }

    public void setRole(String role) { this.role = role; }

    public void addTransactionHistory(List<Object> newTransaction) {this.transactionHistory.add(newTransaction);}

    public void increaseBalance(int balanceToBeAdded) { this.balance += balanceToBeAdded; }

    public void setShoppingCart(ArrayList<Book> shoppingCart) { this.shoppingCart = shoppingCart; }

    public void addBookToCart(Book book) { this.shoppingCart.add(book); }

    public void incPayableAmount(int amount) { this.payableAmount = this.payableAmount+amount;}

    public void deleteShoppingBook(Book book) { this.shoppingCart.remove(book); }

    public void clearCard() {this.shoppingCart.clear();this.payableAmount=0;}
}
