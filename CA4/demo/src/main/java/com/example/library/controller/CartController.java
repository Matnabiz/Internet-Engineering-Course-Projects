package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Order;
import com.example.library.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addOrderToCart(
            @RequestParam String username,
            @RequestParam String bookTitle,
            @RequestBody Order order) {
        return cartService.addOrderToCart(username, bookTitle, order);
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
