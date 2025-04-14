package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.service.AuthorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addAuthor(@RequestBody Map<String, String> body) {
        String adminUsername = body.get("adminUsername");
        String authorName = body.get("authorName");
        String penName = body.getOrDefault("penName", null);
        String nationality = body.get("nationality");
        String birthDate = body.get("birthDate");
        String deathDate = body.getOrDefault("deathDate", null);

        return authorService.addAuthor(adminUsername, authorName, penName, nationality, birthDate, deathDate);
    }



    @GetMapping("/details/{authorName}")
    public ResponseEntity<ResponseWrapper> getAuthorDetails(@PathVariable String authorName) {
        return authorService.showAuthorDetails(authorName);
    }
}
