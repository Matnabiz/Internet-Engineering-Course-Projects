package org.example;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String email;
    private String address;
    private String role;
    private int balance;
    private ArrayList<Book> shoppingCart;

    public User(String username, String password, String email, String address, String role, int balance) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.role = role;
        this.balance = balance;
        this.shoppingCart = new ArrayList<>();
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getAddress() { return address; }

    public String getRole() { return role; }

    public int getBalance() { return balance; }

    public ArrayList<Book> getShoppingCart() { return shoppingCart; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setAddress(String address) { this.address = address; }

    public void setRole(String role) { this.role = role; }

    public void setBalance(int balance) { this.balance = balance; }

    public void setShoppingCart(ArrayList<Book> shoppingCart) { this.shoppingCart = shoppingCart; }

    public void addShoppingBook(Book book) { this.shoppingCart.add(book); }

    public void deleteShoppingBook(Book book) { this.shoppingCart.remove(book); }
}
