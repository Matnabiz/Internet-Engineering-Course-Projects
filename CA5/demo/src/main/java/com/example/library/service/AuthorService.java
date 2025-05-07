package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.entity.AuthorEntity;
import com.example.library.entity.UserEntity;
import com.example.library.model.Author;
import com.example.library.model.Book;
import com.example.library.model.User;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final UserBooksRepository userBooksRepository;
    private final Repository systemData;
    private String message;

    public AuthorService(
            Repository systemData,
            AuthorRepository authorRepository,
            UserRepository userRepository,
            BookRepository bookRepository,
            UserBooksRepository userBooksRepository){
        this.systemData = systemData;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.userBooksRepository = userBooksRepository;
        this.authorRepository = authorRepository;
    }

    public ResponseEntity<ResponseWrapper> addAuthor(
            String adminUsername, String authorName, String authorPenName,
            String nationality, String birthDate, String deathDate) {
        if (!systemData.isLoggedIn(adminUsername)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if (!userRepository.existsByUsername(adminUsername)) {
            message = "User doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        UserEntity adminUser = userRepository.findByUsername(adminUsername).orElseThrow(() -> new RuntimeException("Admin not found."));

        if (!adminUser.getRole().equals("admin")) {
            message = "Only an admin can add authors!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (authorRepository.existsByName(authorName)) {
            message = "Author already exists!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        LocalDate birthDateParsed;
        try {
            birthDateParsed = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            message = "Invalid date of birth format! Use yyyy-mm-dd.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        LocalDate deathDateParsed = null;
        if (deathDate != null) {
            try {
                deathDateParsed = LocalDate.parse(deathDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                message = "Invalid date of death format! Use yyyy-MM-dd.";
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
            }


            if (!Validation.birthBeforeDeath(deathDateParsed, birthDateParsed)) {
                message = "Date of death cannot be before date of birth!";
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
            }
        }

        AuthorEntity newAuthor = new AuthorEntity(adminUsername, authorName, authorPenName, nationality, birthDateParsed, deathDateParsed);
        authorRepository.save(newAuthor);

        message = "Author added successfully!";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, null));
    }

    public ResponseEntity<ResponseWrapper> showAuthorDetails(String authorName){

        if (!systemData.authorExists(authorName)) {
            message = "Author doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Author author = systemData.findAuthor(authorName);
        message = "Author details retrieved successfully.\n";

        ArrayList<Book> searchedBook = (ArrayList<Book>) systemData.books.stream()
                .filter(book -> book.getAuthor().trim().toLowerCase().contains(authorName.toLowerCase()))
                .collect(Collectors.toList());
        ArrayList<Map<String,Object>> authorBooks = new ArrayList<>();
        for(Book book : searchedBook){
            authorBooks.add(Map.of("title",book.getTitle(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> authorData = Map.of(
                "name", author.getName(),
                "penName", author.getPenName(),
                "nationality", author.getNationality(),
                "born", author.getBirthDate(),
                "books", authorBooks
        );

        return ResponseEntity.ok().body(new ResponseWrapper(true, message, authorData));
    }


}
