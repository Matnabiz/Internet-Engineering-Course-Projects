package org.example;

import java.util.ArrayList;
import java.util.Optional;

public class Library {
    private ArrayList<Book> Books = new ArrayList<>();
    private ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Author> Authors = new ArrayList<>();
    private String success;
    private  String message;

    public void addUser(String role, String username, String password, String email, Address address){


    }

    public void addBookToCart (String userName,String bookTitle) {
        boolean existUser = Users.stream().anyMatch(user -> user.getUsername().equals(userName));
        boolean existBook = Books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
        if(!existUser){
            message = "User doesn't exist !";
            success = "false" ;
        }
        if(!existBook){
            message = "Book doesn't exist !";
            success = "false" ;
        }

        Optional<User> Customer = Users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        Book BookToBeAddedToCart = Books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst();

        if(Customer.getRole().equals("admin")) {
            message = "You are admin , you can't buy!";
            success = "false" ;
        }

        else if(Customer.getShoppingCart().length() = 10) {
            message = "Your cart is full!";
            success = "false" ;
        }
        else {
            int i = Users.indexOf(Customer);
            Users.get(i).addShppingBook(BookToBeAddedToCart);
        }
    }

    public void deleteBookFromCart(String userName, String bookTitle){
        boolean existsUser = Users.stream().anyMatch(user -> user.getUsername().equals(userName));
        boolean existsBook = Books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
        if(!existsUser){
            message = "User doesn't exist !";
            success = "false" ;
        }
        if(!existsBook){
            message = "Book doesn't exist not at all!";
            success = "false" ;
        }

        User Buyer = Users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        Book B_Book = Books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst();

        if(Buyer.getRole().equals("admin")) {
            message = "You are admin , you can't do this !";
            success = "false" ;
        }
        else if(!Buyer.getShppingCart().contains(B_Book)){
            message = "You don't have this book in your cart !";
            success = "false" ;
        }
        else {
            int i = Users.indexOf(Buyer);
            Users.get(i).deleteShppingBook(B_Book);
        }
    }
}
