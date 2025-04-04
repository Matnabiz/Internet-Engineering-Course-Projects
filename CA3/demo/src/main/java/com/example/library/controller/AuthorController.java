package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addAuthor(
            @RequestParam String adminUsername,
            @RequestParam String authorName,
            @RequestParam(required = false) String penName,
            @RequestParam String nationality,
            @RequestParam String birthDate,
            @RequestParam(required = false) String deathDate) {

        return authorService.addAuthor(adminUsername, authorName, penName, nationality, birthDate, deathDate);
    }

    @GetMapping("/details/{authorName}")
    public ResponseEntity<ResponseWrapper> getAuthorDetails(@RequestParam String authorName) {
        return authorService.showAuthorDetails(authorName);
    }
}
