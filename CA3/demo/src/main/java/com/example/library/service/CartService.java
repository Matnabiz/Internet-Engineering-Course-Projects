package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Book;
import com.example.library.model.Order;
import com.example.library.model.User;
import com.example.library.repository.Repository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
@Service
public class CartService {

    private final Repository systemData;
    private String message;

    public CartService(Repository systemData){ this.systemData = systemData; }

    public ResponseEntity<ResponseWrapper> addOrderToCart (String username, String bookTitle, Order orderToBeAddedToCart) {
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if (!systemData.userExists(username)) {
            message = "User doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!systemData.bookExists(bookTitle)) {
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (Validation.customerHasBookInCart(systemData.findUser(username), bookTitle)){
            message = "You already have this book in your cart!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(systemData.findUser(username).userHasAccessToBook(bookTitle)){
            message = "You already have access to this book!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Book bookToBeAddedToCart = systemData.findBook(bookTitle);
        orderToBeAddedToCart.setBook(bookToBeAddedToCart);

        User customer = systemData.findUser(username);
        if (customer.getRole().equals("admin")) {
            message = "You are an admin, you can't buy!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        } else if (Validation.cartIsFull(customer)) {
            message = "Your cart is full!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (orderToBeAddedToCart.getType() == "buy") {
            customer.addOrderToCart(orderToBeAddedToCart);
            message = "Book added to cart.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else if (orderToBeAddedToCart.getType() == "borrow") {
            customer.addOrderToCart(orderToBeAddedToCart);
            message = "Book added to cart.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else {
            message = "Invalid purchase type.";
            return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
        }
    }

    public ResponseEntity<ResponseWrapper> removeOrderFromCart(String username, String bookTitle){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!systemData.userExists(username)){
            message = "User doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!systemData.bookExists(bookTitle)){
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User customer = systemData.findUser(username);
        Book bookToBeRemovedFromCart = systemData.findBook(bookTitle);

        if(customer.getRole().equals("admin")) {
            message = "You are admin, you can't do this!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else if(!Validation.customerHasBookInCart(customer, bookToBeRemovedFromCart.getTitle())){
            message = "You don't have this book in your cart!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        else {
            customer.removeOrderFromCart(bookToBeRemovedFromCart);
            message = "Book removed from cart successfully!";
            return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
        }
    }

    public ResponseEntity<ResponseWrapper> purchaseCart(String username){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!systemData.userExists(username)){
            message = "this username doesn't exist in system";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User customer = systemData.findUser(username);

        if(!Validation.minimumBookCountInCartForCheckout(customer)){
            message = "Cart is empty!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!Validation.enoughBalanceForCheckout(customer)){
            message = "Balance isn't enough for purchase!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        LocalDateTime purchaseTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = purchaseTime.format(formatter);
        message = "Purchase completed successfully.";
        Map<String, Object> userData = Map.of(
                "bookCount", customer.getShoppingCart().size(),
                "totalCost", customer.getPayableAmount(),
                "date", formattedTime
        );
        customer.updateInfoAfterCheckout();
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, userData));
    }

    public ResponseEntity<ResponseWrapper> showCart(String username){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!systemData.userExists(username)){
            message = "User doesn't exist";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User u_user= systemData.findUser(username);
        if (u_user.getRole().equals("admin")) {
            message = "This command isn't for admins!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        ArrayList<Map<String,Object>> bookInCard = new ArrayList<>();
        for(Order order : u_user.getShoppingCart()){
            bookInCard.add(Map.of("title",order.getBook().getTitle(),
                    "author",order.getBook().getAuthor(),
                    "publisher",order.getBook().getPublisher(),
                    "genres",order.getBook().getGenres(),
                    "year",order.getBook().getPublicationYear(),
                    "price",order.getBook().getPrice(),
                    "isBorrowed",order.getType().equals("borrow"),
                    "finalPrice",order.getOrderPrice(),
                    "borrowDays",order.getBorrowDurationDays()));
        }

        Map<String, Object> cartDetails = Map.of(
                "username", username,
                "totalCost",u_user.getPayableAmount(),
                "items",bookInCard
        );
        message="Buy cart retrieved successfully.";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, cartDetails));

    }

}
