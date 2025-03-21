package com.example.controller;

import com.example.model.User;
import com.example.dto.ResponseWrapper;
import com.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> showUserDetails(@PathVariable String username) {
        return userService.showUserDetails(username);
    }

    @PostMapping("/{username}/credit")
    public ResponseEntity<ResponseWrapper> addCredit(@PathVariable String username, @RequestParam double amount) {
        return userService.addCredit(username, amount);
    }

    @GetMapping("/{username}/purchases")
    public ResponseEntity<ResponseWrapper> showPurchaseHistory(@PathVariable String username) {
        return userService.showPurchaseHistory(username);
    }

    @GetMapping("/{username}/purchased-books")
    public ResponseEntity<ResponseWrapper> showPurchasedBooks(@PathVariable String username) {
        return userService.showPurchasedBooks(username);
    }
}
