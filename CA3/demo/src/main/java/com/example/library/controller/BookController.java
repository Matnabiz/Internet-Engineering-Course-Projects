package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseWrapper> addBook(
            @RequestParam String username,
            @RequestParam String bookTitle,
            @RequestParam String bookAuthor,
            @RequestParam String bookPublisher,
            @RequestParam int publishYear,
            @RequestParam ArrayList<String> bookGenres,
            @RequestParam String bookContent,
            @RequestParam String bookSynopsys,
            @RequestParam int bookPrice) {

        return bookService.addBook(username, bookTitle, bookAuthor, bookPublisher, publishYear, bookGenres, bookContent, bookSynopsys, bookPrice);
    }

    @GetMapping("/details")
    public ResponseEntity<ResponseWrapper> getBookDetails(@RequestParam String bookTitle) {
        return bookService.showBookDetails(bookTitle);
    }

    @GetMapping("/reviews")
    public ResponseEntity<ResponseWrapper> getBookReviews(@RequestParam String bookTitle) {
        return bookService.showBookReviews(bookTitle);
    }

    @GetMapping("/content")
    public ResponseEntity<ResponseWrapper> getBookContent(
            @RequestParam String username,
            @RequestParam String bookTitle) {
        return bookService.showBookContent(username, bookTitle);
    }
}
