package com.example.library.repository;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component

public class Repository {
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();
    public ArrayList<Author> authors = new ArrayList<>();
    public final Set<String> loggedInUsers = new HashSet<>();

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

    public boolean isLoggedIn(String username) {
        return this.loggedInUsers.contains(username);
    }
    public ArrayList<Map<String, Object>> retrieveUserBooks(String username){
        User user = this.findUser(username);
        ArrayList<Map<String, Object>> purchasedBooks = new ArrayList<>();
        for(int j=0;j<user.getTransactionHistory().size();j++) {
            for (int i = 0; i < user.getTransactionHistory().get(j).size() - 3; i++) {
                if (user.getTransactionHistory().get(j).get(i) instanceof Order) {
                    Order order = (Order) user.getTransactionHistory().get(j).get(i);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime givenDate = LocalDateTime.parse(String.valueOf(user.getTransactionHistory().get(j).get(user.getTransactionHistory().get(j).size() - 1)), formatter);
                    LocalDateTime now = LocalDateTime.now();
                    long daysPassed = ChronoUnit.DAYS.between(givenDate, now);
                    if (order.getBorrowDurationDays() > daysPassed || order.getBorrowDurationDays() == 0) { // ----> check for bought book not borrowed
                        purchasedBooks.add(Map.of("title", order.getBook().getTitle(),
                                "author", order.getBook().getAuthor(),
                                "publisher", order.getBook().getPublisher(),
                                "category", order.getBook().getGenres(),
                                "year", order.getBook().getPublicationYear(),
                                "price", order.getBook().getPrice(),
                                "isBorrowed", order.getType().equals("borrow")));
                    }
                }
            }
        }
        return purchasedBooks;
    }

}
