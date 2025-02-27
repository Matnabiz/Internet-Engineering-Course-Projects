package org.example;

import java.util.ArrayList;
import java.util.Optional;

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
