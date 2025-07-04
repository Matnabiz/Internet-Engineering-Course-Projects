package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.entity.AddressEmbeddable;
import com.example.library.entity.UserEntity;
import com.example.library.model.*;
import com.example.library.repository.*;
import com.example.library.entity.*;
import com.example.library.repository.Repository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {


    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final UserBooksRepository userBooksRepository;
    @Autowired
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UserService(
            UserRepository userRepository,
            BookRepository bookRepository,
            UserBooksRepository userBooksRepository, StringRedisTemplate redisTemplate,
            Repository systemData) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBooksRepository = userBooksRepository;
        this.redisTemplate = redisTemplate;
        this.systemData = systemData;
    }
    private final Repository systemData;
    private String message;

    @PostConstruct
    public void testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("test", "ok", 10, TimeUnit.SECONDS);
            System.out.println("✅ Redis connection successful.");
        } catch (Exception e) {
            System.err.println("❌ Redis connection failed: " + e.getMessage());
        }
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

        String token = UUID.randomUUID().toString();
        String redisKey = token+":"+username;
        String userData = username;
        redisTemplate.opsForValue().set(redisKey,userData,1, TimeUnit.MINUTES);
        systemData.loggedInUsers.add(username);
        return ResponseEntity.ok(new ResponseWrapper(true, "Login Successful",token));
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

        User newUser = new User(username, password, email, address, role.toLowerCase(), 0);
        systemData.addUser(newUser);
        UserEntity newUserEntity = new UserEntity(username, password, email, role, 0, new AddressEmbeddable((String) address.userCountry, (String) address.userCity));
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

        User customer = systemData.findUser(username);
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
            customer.increaseBalance(credit);
            userAddingCredit.setBalance(userAddingCredit.getBalance() + credit);
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

        if(!userRepository.existsByUsername(username)){
            message = "this username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        List<UserBooksEntity> booksInAccess = UserBooksRepository.findByUserUsername(username);
        if (booksInAccess == null || booksInAccess.isEmpty()) {
            throw new RuntimeException("No books");
        }


        Map<String, Object> finalPurchasedBook = Map.of("books", booksInAccess);
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
                        "year", order.getBook().getPublicationYear(),
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
        if (!systemData.userExists(username)) {
            message = "User doesn't exist";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        User user = systemData.findUser(username);
        if (user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        ArrayList<Map<String, Object>> booksInAccess = systemData.retrieveUserBooks(username);
        Map<String, Object> finalPurchasedBook = Map.of(
                "username", username,
                "books", booksInAccess
        );
        message = "Purchased books retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, finalPurchasedBook));
    }

}
