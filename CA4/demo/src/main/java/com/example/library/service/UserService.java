package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.*;
import com.example.library.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
@Service
public class UserService {

    private final Repository systemData;
    private String message;
    public UserService(Repository systemData) {
        this.systemData = systemData;
    }

    public ResponseEntity<ResponseWrapper> logoutUser(String username) {
        if (!systemData.isLoggedIn(username)) {
            message = "This user is not signed in.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        systemData.loggedInUsers.remove(username);
        message = "Logout successful.";
        return ResponseEntity.ok(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> loginUser(String username, String password) {
        if (!systemData.userExists(username)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper(false, "User not found!", null));
        }

        if (systemData.isLoggedIn(username)) {
            message = "This user has already signed in.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        //   mahsa gogoli and matin bolbol
        User userAskingToLogIn = systemData.findUser(username);

        if (!Validation.authenticatePassword(password, userAskingToLogIn)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseWrapper(false, "Invalid credentials.", null));
        }

        systemData.loggedInUsers.add(username);
        return ResponseEntity.ok(new ResponseWrapper(true, "Login Successful", null));
    }

    public ResponseEntity<ResponseWrapper> addUser(String username, String password, String email, Address address, String role) {

        if (!Validation.validateUsername(username)) {
            message = "Invalid username! Only letters, numbers, underscore (_), and hyphen (-) are allowed.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }


        if (systemData.userExists(username)) {
            message = "Username already exists! Please choose a different one.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (systemData.emailExists(email)) {
            message = "Email address already registered! Please use a different one.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (Validation.validatePassword(password)) {
            message = "Password must be at least 4 characters long!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!Validation.validateEmail(email)) {
            message = "Invalid email format! Example: example@test.com";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!Validation.validateRole(role)) {
            message = "Invalid role! Role must be either 'customer' or 'admin'.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User newUser = new User(username, password, email, address, role.toLowerCase(), 0);
        systemData.users.add(newUser);

        message = "User successfully registered!";
        return ResponseEntity.ok(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> addCredit(String username,int credit){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!systemData.userExists(username)){
            message = "This username doesn't exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User customer = systemData.findUser(username);
        if(customer.getRole().equals("admin")) {
            message = "An admin can't add credit!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!Validation.minimumCreditForBalanceCharge(credit)){
            message = "You should charge at least 1$ or 100 cents!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else {
            customer.increaseBalance(credit);
            message = "Credit added successfully.";
            return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
        }
    }

    public ResponseEntity<ResponseWrapper> addComment(String username, String bookTitle, String commentBody, int rating) {
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        User customer = systemData.findUser(username);
        Book book = systemData.findBook(bookTitle);

        if(!systemData.userExists(username)){
            message = "This username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(customer.getRole().equals("admin")) {
            message = "As an admin, You can't submit a comment.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!systemData.bookExists(bookTitle)){
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!Validation.ratingInRange(rating)) {
            message = "Ratings can only be a natural number between 1 and 5!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!customer.userHasAccessToBook(bookTitle)){
            message = "You can't submit a comment for this book.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Comment newComment = new Comment(username, commentBody, rating);
        book.addComment(newComment);
        message = "Review added successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> showUserDetails(String username) {

        if(!systemData.userExists(username)){
            message = "this username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        message = "User details retrieved successfully.\n";
        User user = systemData.findUser(username);
        Map<String, Object> userData = Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "address", user.getAddress(),
                "role", user.getRole(),
                "balance", user.getBalance()
        );

        return ResponseEntity.ok().body(new ResponseWrapper(true, message, userData));
    }

    public ResponseEntity<ResponseWrapper> showPurchaseHistory(String username) {

        if (!systemData.userExists(username)) {
            message = "User doesn't exist";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User customer = systemData.findUser(username);
        if (customer.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        ArrayList<Map<String, Object>> bookItems = new ArrayList<>();
        for (int i = 0; i < customer.getTransactionHistory().size() - 3; i++) {
            if (customer.getTransactionHistory().get(i) instanceof Order) {
                Order order = (Order) customer.getTransactionHistory().get(i); // Cast to Book
                bookItems.add(Map.of("title", order.getBook().getTitle(),
                        "author", order.getBook().getAuthor(),
                        "publisher", order.getBook().getPublisher(),
                        "genres", order.getBook().getGenres(),
                        "year", order.getBook().getPublicationYear(),
                        "isBorrowed", order.getType().equals("borrow"),
                        "price", order.getBook().getPrice(),
                        "finalPrice", order.getOrderPrice()));
            }
        }

        Map<String, Object> purchaseHistory = Map.of(
                "purchaseDate", customer.getTransactionHistory().get(customer.getTransactionHistory().size()- 1),
                "items", bookItems,
                "totalCost", customer.getTransactionHistory().get(customer.getTransactionHistory().size() - 2));


        Map<String, Object> finalPurchaseHistory = Map.of(
                "username", username,
                "purchaseHistory", purchaseHistory
        );

        message = "Purchase history retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, finalPurchaseHistory));
    }

    public ResponseEntity<ResponseWrapper> showPurchasedBooks (String username){
        if (!systemData.userExists(username)) {
            message = "User doesn't exist";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User user = systemData.findUser(username);
        if (user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

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

        Map<String, Object> finalPurchasedBook = Map.of(
                "username", username,
                "books", purchasedBooks
        );
        message = "Purchased books retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, finalPurchasedBook));
    }

}
