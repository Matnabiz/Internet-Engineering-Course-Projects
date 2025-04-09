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
        return authorService.addAuthor(
                body.get("adminUsername"),
                body.get("authorName"),
                body.get("penName"),
                body.get("nationality"),
                body.get("birthDate"),
                body.get("deathDate")
        );
    }


    @GetMapping("/details/{authorName}")
    public ResponseEntity<ResponseWrapper> getAuthorDetails(@PathVariable String authorName) {
        return authorService.showAuthorDetails(authorName);
    }
}
