package com.example.library.repository;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.User;

import java.util.ArrayList;
@Component

public class Repository {
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Author> authors = new ArrayList<>();

    public boolean userExists(String username){
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    public boolean bookExists(String bookTitle){
        return books.stream().anyMatch(book -> book.getTitle().equals(bookTitle));
    }

    public boolean authorExists(String name){
        return authors.stream().anyMatch(a -> a.getName().equals(name));
    }

    public boolean emailExists(String email){
        return users.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public User findUser(String username){
        return this.users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public Author findAuthor(String name){
        return this.authors.stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public Book findBook(String bookTitle){
        return this.books.stream()
                .filter(b -> b.getTitle().equals(bookTitle))
                .findFirst()
                .orElse(null);
    }


}
