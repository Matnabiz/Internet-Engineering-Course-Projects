package com.example.library.controller;
import java.util.List;
import java.util.ArrayList;
import com.example.library.dto.ResponseWrapper;
import com.example.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addBook(@RequestBody Map<String, Object> body) {
        List<String> genres = (List<String>) body.get("Genres");
        return bookService.addBook(
                (String) body.get("username"),
                (String) body.get("Title"),
                (String) body.get("Author"),
                (String) body.get("Publisher"),
                (Integer) body.get("Year"),
                new ArrayList<>(genres),
                (String) body.get("Content"),
                (String) body.get("Synopsis"),
                (Integer) body.get("Price")
                );
    }

    @GetMapping("/details/{bookTitle}")
    public ResponseEntity<ResponseWrapper> getBookDetails(@PathVariable String bookTitle) {
        return bookService.showBookDetails(bookTitle);
    }

    @GetMapping("/reviews/{bookTitle}")
    public ResponseEntity<ResponseWrapper> getBookReviews(@PathVariable String bookTitle) {
        return bookService.showBookReviews(bookTitle);
    }

    @GetMapping("/content")
    public ResponseEntity<ResponseWrapper> getBookContent(@RequestBody Map<String, Object> body) {
        return bookService.showBookContent((String) body.get("username"), (String) body.get("Title"));
    }
}
