package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Address;
import com.example.library.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> addUser(@RequestBody Map<String, Object> body){
        Map<String, String> addressMap = (Map<String, String>) body.get("address");

        Address address = new Address(
                addressMap.get("country"),
                addressMap.get("city")
        );

        return userService.addUser(
                (String) body.get("username"),
                (String) body.get("password"),
                (String) body.get("email"),
                address,
                (String) body.get("role")
        );
    }


    @PostMapping("/{username}/add-credit")
    public ResponseEntity<ResponseWrapper> addCredit(
            @PathVariable String username,
            @RequestParam int credit) {
        return userService.addCredit(username, credit);
    }

    @PostMapping("/{username}/add-comment")
    public ResponseEntity<ResponseWrapper> addComment(
            @PathVariable String username,
            @RequestParam String bookTitle,
            @RequestParam String commentBody,
            @RequestParam int rating) {
        return userService.addComment(username, bookTitle, commentBody, rating);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ResponseWrapper> showUserDetails(@PathVariable String username) {
        return userService.showUserDetails(username);
    }

    @GetMapping("/{username}/history")
    public ResponseEntity<ResponseWrapper> showPurchaseHistory(@PathVariable String username) {
        return userService.showPurchaseHistory(username);
    }

    @GetMapping("/{username}/purchased-books")
    public ResponseEntity<ResponseWrapper> showPurchasedBooks(@PathVariable String username) {
        return userService.showPurchasedBooks(username);
    }
}
