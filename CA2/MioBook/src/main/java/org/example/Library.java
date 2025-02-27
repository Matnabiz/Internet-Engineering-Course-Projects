package org.example;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;


public class Library {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Author> authors = new ArrayList<>();
    private String success;
    private  String message;

    public void addUser(String role, String username, String password, String email, Address address){


    }

    private boolean userExists(String username){
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private boolean bookExists(String bookTitle){
        return books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
    }

    private boolean emailExists(String email){
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    private boolean validateUsername(String username){
        return Pattern.matches("^[a-zA-Z0-9_-]+$", username);
    }

    private boolean validatePassword(String password){
        return password.length() < 4;
    }

    private boolean validateEmail(String email){
        return Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", email);
    }

    private boolean validateRole(String role){
        return role.equalsIgnoreCase("customer") && !role.equalsIgnoreCase("admin");
    }
    public void addUser(String username, String password, String email, String address, String role) {

        if (!validateUsername(username)) {
            message = "Invalid username! Only letters, numbers, underscore (_), and hyphen (-) are allowed.";
            success = "false";
            return;
        }


        if (userExists(username)) {
            message = "Username already exists! Please choose a different one.";
            success = "false";
            return;
        }

        if (emailExists(email)) {
            message = "Email address already registered! Please use a different one.";
            success = "false";
            return;
        }

        if (validatePassword(password)) {
            message = "Password must be at least 4 characters long!";
            success = "false";
            return;
        }

        if (!validateEmail(email)) {
            message = "Invalid email format! Example: example@test.com";
            success = "false";
            return;
        }

        if (!validateRole(role)) {
            message = "Invalid role! Role must be either 'customer' or 'admin'.";
            success = "false";
            return;
        }

        User newUser = new User(username, password, email, address, role.toLowerCase(), 0);
        users.add(newUser);

        message = "User successfully registered!";
        success = "true";
    }
    public void addBookToCart (String userName, String bookTitle) {
        if(!userExists(userName)){
            message = "User doesn't exist!";
            success = "false" ;
            return;
        }
        if(!bookExists(bookTitle)){
            message = "Book doesn't exist!";
            success = "false" ;
            return;
        }

        User Customer = users.stream()
                .filter(u -> u.getUsername().equals(userName))
                .findFirst()
                .orElse(null);

        Book BookToBeAddedToCart = books.stream()
                .filter(b -> b.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);
        if (Customer == null || BookToBeAddedToCart == null) return;

        if(Customer.getRole().equals("admin")) {
            message = "You are an admin, you can't buy!";
            success = "false" ;
        }

        if (Customer.getShoppingCart().size() == 10) {
            message = "Your cart is full!";
            success = "false" ;
        }
        else {
            int i = users.indexOf(Customer);
            users.get(i).addBookToCart(BookToBeAddedToCart);
        }
    }

    public void deleteBookFromCart(String userName, String bookTitle){
        boolean existsUser = users.stream().anyMatch(user -> user.getUsername().equals(userName));
        boolean existsBook = books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
        if(!existsUser){
            message = "User doesn't exist!";
            success = "false" ;
        }
        if(!existsBook){
            message = "Book doesn't exist!";
            success = "false" ;
        }

        Optional<User> Buyer = users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        Optional<Book> B_Book = books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst();

        if(Buyer.getRole().equals("admin")) {
            message = "You are admin , you can't do this !";
            success = "false" ;
        }
        else if(!Buyer.getShppingCart().contains(B_Book)){
            message = "You don't have this book in your cart !";
            success = "false" ;
        }
        else {
            int i = users.indexOf(Buyer);
            users.get(i).deleteShppingBook(B_Book);
        }
    }
}
