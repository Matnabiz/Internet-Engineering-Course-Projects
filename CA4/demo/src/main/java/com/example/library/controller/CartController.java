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
    public ResponseEntity<ResponseWrapper> removeOrderFromCart(@RequestBody Map<String, Object> body) {
        return cartService.removeOrderFromCart((String) body.get("username"), (String) body.get("bookTitle"));
    }

    @PostMapping("/purchase/{username}")
    public ResponseEntity<ResponseWrapper> purchaseCart(@PathVariable String username) {
        return cartService.purchaseCart(username);
    }

    @GetMapping("/show/{username}")
    public ResponseEntity<ResponseWrapper> showCart(@PathVariable String username) {
        return cartService.showCart(username);
    }
}
