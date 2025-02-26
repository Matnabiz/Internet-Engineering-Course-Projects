package org.example;

import java.util.ArrayList;

public class Library {
    private ArrayList<Book> Books = new ArrayList<>();
    private ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Author> Authors = new ArrayList<>();
    private String success;
    private  String message;

    public void addCart (String userName,String bookTitle) {
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

        User Buyer = Users.stream().filter(u -> u.getUsername().equals(userName)).findFirst();
        Book B_Book = Books.stream().filter(b -> b.getTitle().equals(bookTitle)).findFirst();

        if(Buyer.getRole().equals("admin")) {
            message = "You are admin , you can't buy !";
            success = "false" ;
        }

        else if(Buyer.getShoppingCart().length() = 10) {
            message = "Your card is full !";
            success = "false" ;
        }
        else {
            int i = Users.indexOf(Buyer);
            Users.get(i).addShppingBook(B_Book);
        }
    }

    public void deleteBook(String userName,String bookTitle){
        boolean existUser = Users.stream().anyMatch(user -> user.getUsername().equals(userName));
        boolean existBook = Books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
        if(!existUser){
            message = "User doesn't exist !";
            success = "false" ;
        }
        if(!existBook){
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
