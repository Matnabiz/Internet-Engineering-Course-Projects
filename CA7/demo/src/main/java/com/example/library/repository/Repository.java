package com.example.library.repository;

import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.model.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    public void addAuthor(Author author) { this.authors.add(author); }
    public void addUser(User user){ this.users.add(user); }
    public void addBook(Book book){ this.books.add(book); }
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
    public ArrayList<Map<String, Object>> retrieveUserBooks(String username) {
        User user = this.findUser(username);
        ArrayList<Map<String, Object>> purchasedBooks = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        for (Transaction transaction : user.getTransactionHistory()) {
            LocalDateTime transactionTime;
            try {
                transactionTime = LocalDateTime.parse(transaction.getTime(), formatter);
            } catch (DateTimeParseException e) {
                continue; // Skip this transaction if time is malformed
            }

            for (Order order : transaction.getOrders()) {
                if (order == null) continue;

                int borrowDays = order.getBorrowDurationDays();
                long daysPassed = ChronoUnit.DAYS.between(transactionTime, now);

                boolean isOwned = borrowDays == 0;
                boolean isStillBorrowed = borrowDays > daysPassed;
                if (isOwned || isStillBorrowed) {
                    Map<String, Object> bookInfo = Map.of(
                            "title", order.getBook().getTitle(),
                            "author", order.getBook().getAuthor(),
                            "publisher", order.getBook().getPublisher(),
                            "category", order.getBook().getGenres(),
                            "year", order.getBook().getYear(),
                            "price", order.getBook().getPrice(),
                            "isBorrowed", order.getType().equals("borrow")
                    );
                    purchasedBooks.add(bookInfo);
                }
            }
        }

        return purchasedBooks;
    }


}
