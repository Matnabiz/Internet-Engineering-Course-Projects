package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.entity.*;
import com.example.library.model.Order;
import com.example.library.repository.BookRepository;
import com.example.library.repository.OrderRepository;
import com.example.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CartService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final Map<String, List<Order>> userCarts = new HashMap<>(); // Simulated in-memory cart
    private String message;

    @Autowired
    public CartService(UserRepository userRepository,
                       BookRepository bookRepository,
                       OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    public ResponseEntity<ResponseWrapper> addOrderToCart(String username, String bookTitle, Order order) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        Optional<BookEntity> bookOpt = bookRepository.findByTitle(bookTitle);

        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "User doesn't exist!", null));
        }
        if (bookOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Book doesn't exist!", null));
        }

        UserEntity user = userOpt.get();
        BookEntity book = bookOpt.get();

        if (user.getRole().equals("admin")) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Admins can't buy books!", null));
        }

        List<Order> cart = userCarts.computeIfAbsent(username, k -> new ArrayList<>());
        if (cart.stream().anyMatch(o -> o.getBook().getId().equals(book.getId()))) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Book already in cart!", null));
        }

        boolean alreadyOwned = orderRepository.existsByUserAndBook(user, book);
        if (alreadyOwned) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "You already have this book!", null));
        }

        order.setBook(book);
        cart.add(order);
        return ResponseEntity.ok().body(new ResponseWrapper(true, "Book added to cart.", null));
    }

    public ResponseEntity<ResponseWrapper> removeOrderFromCart(String username, String bookTitle) {
        List<Order> cart = userCarts.get(username);
        if (cart == null || cart.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Cart is empty!", null));
        }

        boolean removed = cart.removeIf(order -> order.getBook().getTitle().equalsIgnoreCase(bookTitle));
        if (!removed) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Book not found in cart!", null));
        }

        return ResponseEntity.ok().body(new ResponseWrapper(true, "Book removed from cart.", null));
    }

    public ResponseEntity<ResponseWrapper> showCart(String username) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "User doesn't exist!", null));
        }

        UserEntity user = userOpt.get();
        if (user.getRole().equals("admin")) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Admins don't have a cart!", null));
        }

        List<Order> cart = userCarts.getOrDefault(username, new ArrayList<>());
        List<Map<String, Object>> items = new ArrayList<>();
        int totalCost = 0;

        for (Order order : cart) {
            BookEntity book = order.getBook();
            int finalPrice = (int) order.getOrderPrice();
            totalCost += finalPrice;

            items.add(Map.of(
                    "title", book.getTitle(),
                    "author", book.getAuthor(),
                    "publisher", book.getPublisher(),
                    "genres", book.getGenres(),
                    "year", book.getYear(),
                    "price", book.getPrice(),
                    "isBorrowed", order.getType().equals("borrow"),
                    "finalPrice", finalPrice,
                    "borrowDays", order.getBorrowDurationDays()
            ));
        }

        Map<String, Object> response = Map.of(
                "username", username,
                "totalCost", totalCost,
                "items", items
        );

        return ResponseEntity.ok().body(new ResponseWrapper(true, "Cart retrieved successfully.", response));
    }

    public ResponseEntity<ResponseWrapper> purchaseCart(String username) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "User not found.", null));
        }

        UserEntity user = userOpt.get();
        List<Order> cart = userCarts.getOrDefault(username, new ArrayList<>());

        if (cart.isEmpty()) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Cart is empty!", null));
        }

        int totalCost = cart.stream().mapToInt(Order::getOrderPrice).sum();
        if (user.getBalance() < totalCost) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "Insufficient balance.", null));
        }

        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        for (Order order : cart) {
            BookEntity book = order.getBook();
            OrderEntity userBook = new OrderEntity(
                    user, book,
                    order.getType().equals("borrow"),
                    order.getType().equals("borrow") ? order.getBorrowDurationDays() : null,
                    order.getType().equals("borrow") ? formattedDate : null
            );
            orderRepository.save(userBook);
        }

        user.setBalance(user.getBalance() - totalCost);
        userRepository.save(user);
        userCarts.remove(username);

        Map<String, Object> result = Map.of(
                "bookCount", cart.size(),
                "totalCost", totalCost,
                "date", formattedDate
        );

        return ResponseEntity.ok().body(new ResponseWrapper(true, "Purchase successful.", result));
    }
}
