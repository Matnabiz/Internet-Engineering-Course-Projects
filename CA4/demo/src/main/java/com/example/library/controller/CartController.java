package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Order;
import com.example.library.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/buy/add")
    public ResponseEntity<ResponseWrapper> addBuyingOrderToCart(@RequestBody Map<String, Object> body) {

        Order orderToBeAddedToCart = new Order(null, "buy", 0);

        return cartService.addOrderToCart((String) body.get("username"), (String) body.get("bookTitle"), orderToBeAddedToCart);
    }

    @PostMapping("/borrow/add")
    public ResponseEntity<ResponseWrapper> addBorrowingOrderToCart(@RequestBody Map<String, Object> body) {

        Order orderToBeAddedToCart = new Order(null, "borrow", (Integer) body.get("days"));

        return cartService.addOrderToCart((String) body.get("username"), (String) body.get("bookTitle"), orderToBeAddedToCart);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ResponseWrapper> removeOrderFromCart(
            @RequestParam String username,
            @RequestParam String bookTitle) {
        return cartService.removeOrderFromCart(username, bookTitle);
    }

    @PostMapping("/purchase")
    public ResponseEntity<ResponseWrapper> purchaseCart(@RequestParam String username) {
        return cartService.purchaseCart(username);
    }

    @GetMapping("/show")
    public ResponseEntity<ResponseWrapper> showCart(@RequestParam String username) {
        return cartService.showCart(username);
    }
}
