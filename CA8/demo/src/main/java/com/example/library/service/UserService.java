package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.*;
import com.example.library.repository.*;
import com.example.library.entity.*;
import com.example.library.repository.Repository;
import com.example.library.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    public UserService(
            UserRepository userRepository,
            BookRepository bookRepository,
            ReviewRepository reviewRepository,
            OrderRepository orderRepository,
            Repository systemData) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.systemData = systemData;
        this.orderRepository = orderRepository;
    }
    private final Repository systemData;
    private String message;


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
        if (!userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper(false, "User not found!", null));
        }

        if (systemData.isLoggedIn(username)) {
            message = "This user has already signed in.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        UserEntity userLoggingIn = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (!Validation.authenticatePassword(password, userLoggingIn)) {
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


        if (userRepository.existsByUsername(username)) {
            message = "Username already exists! Please choose a different one.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (userRepository.existsByEmail(email)) {
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

        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(password, salt);
        UserEntity newUserEntity = new UserEntity(username, hashedPassword, salt, email, role, 0, new AddressEmbeddable((String) address.userCountry, (String) address.userCity));
        userRepository.save(newUserEntity);

        message = "User successfully registered!";
        return ResponseEntity.ok(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> addCredit(String username,int credit){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!userRepository.existsByUsername(username)){
            message = "This username doesn't exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        UserEntity userAddingCredit = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        if(userAddingCredit.getRole().equals("admin")) {
            message = "An admin can't add credit!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!Validation.minimumCreditForBalanceCharge(credit)){
            message = "You should charge at least 1$ or 100 cents!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else {
            int balanceToBeAdded = userAddingCredit.getBalance();
            userAddingCredit.setBalance(balanceToBeAdded + credit);
            userRepository.save(userAddingCredit);
            message = "Credit added successfully.";
            return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
        }
    }

    public ResponseEntity<ResponseWrapper> addComment(String username, String bookTitle, String commentBody, int rating) {
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!userRepository.existsByUsername(username)){
            message = "This username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        UserEntity customer = userRepository.findByUsername(username).orElseThrow(
                () -> new RuntimeException("User not found.")
        );
        BookEntity book = bookRepository.findByTitle(bookTitle).orElseThrow(
                () -> new RuntimeException("Book not found.")
        );

        if(customer.getRole().equals("admin")) {
            message = "As an admin, You can't submit a comment.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!bookRepository.existsByTitle(bookTitle)){
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!Validation.ratingInRange(rating)) {
            message = "Ratings can only be a natural number between 1 and 5!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!userHasAccessToBook(customer, book)){
            message = "You can't submit a comment for this book.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        ReviewEntity newReview = new ReviewEntity(customer.getUsername(), book.getTitle(), commentBody, rating);
        reviewRepository.save(newReview);

        message = "Review added successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> showUserDetails(String username) {

        if(!userRepository.existsByUsername(username)){
            message = "this username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        List<OrderEntity> booksInAccess = orderRepository.findByUserUsername(username);
        if (booksInAccess == null) { booksInAccess = new ArrayList<>(); }
        message = "User details retrieved successfully.\n";
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Map<String, Object> userData = Map.of(
                "username", user.getUsername(),
                "email", user.getEmail(),
                "address", user.getAddress(),
                "role", user.getRole(),
                "balance", user.getBalance(),
                "books", booksInAccess
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

        List<Map<String, Object>> purchaseHistoryList = new ArrayList<>();
        List<Transaction> history = customer.getTransactionHistory();
        for (Transaction transaction : history) {
            List<Map<String, Object>> orderItems = new ArrayList<>();

            for (Order order : transaction.getOrders()) {
                orderItems.add(Map.of(
                        "title", order.getBook().getTitle(),
                        "author", order.getBook().getAuthor(),
                        "publisher", order.getBook().getPublisher(),
                        "genres", order.getBook().getGenres(),
                        "year", order.getBook().getYear(),
                        "isBorrowed", order.getType().equalsIgnoreCase("borrow"),
                        "price", order.getBook().getPrice(),
                        "finalPrice", order.getOrderPrice()
                ));
            }

            Map<String, Object> transactionData = Map.of(
                    "purchaseDate", transaction.getTime(),
                    "items", orderItems,
                    "totalCost", transaction.getTotalPrice()
            );

            purchaseHistoryList.add(transactionData);
        }

        Map<String, Object> responseData = Map.of(
                "username", username,
                "purchaseHistory", purchaseHistoryList
        );

        message = "Purchase history retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, responseData));
    }

    public ResponseEntity<ResponseWrapper> showPurchasedBooks (String username){
        if (!userRepository.existsByUsername(username)) {
            message = "User doesn't exist";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User doesn't exist"));
        if (user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        List<Map<String, Object>> booksInAccess = orderRepository.retrieveUserBooks(username);
        Map<String, Object> finalPurchasedBook = Map.of(
                "username", username,
                "books", booksInAccess
        );
        message = "Purchased books retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, finalPurchasedBook));
    }

    public boolean userHasAccessToBook(UserEntity user, BookEntity book){
        return orderRepository.existsByUserAndBook(user, book);
    }

}
