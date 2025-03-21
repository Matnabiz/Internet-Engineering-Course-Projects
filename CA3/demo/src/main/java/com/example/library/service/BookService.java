package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Book;
import com.example.library.model.Comment;
import com.example.library.model.User;
import com.example.library.repository.Repository;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;

public class BookService {

    private final Repository systemData;
    private String message;

    public BookService(Repository systemData){ this.systemData = systemData; }

    public ResponseEntity<ResponseWrapper> addBook (String username, String bookTitle,
                                                    String bookAuthor, String bookPublisher,
                                                    int publishYear, ArrayList<String> bookGenres,
                                                    String bookContent, String bookSynopsys, int bookPrice){

        if(!systemData.userExists(username)){
            message = "This username doesn't exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(systemData.bookExists(bookTitle)){
            message = "This book already exist in system !";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!systemData.authorExists(bookAuthor)){
            message = "The author of this book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        User bookAdder= systemData.findUser(username);
        if (!bookAdder.getRole().equals("admin")) {
            message = "Only an admin can add books!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!Validation.minimumGenreCount(bookGenres)){
            message = "A Book should at least have one genre!" ;
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Book newBook = new Book(bookTitle, bookAuthor,
                bookPublisher, publishYear,
                bookGenres,bookPrice,bookSynopsys,bookContent);
        systemData.books.add(newBook);
        message =  "Book added successfully.";
        return ResponseEntity.ok(new ResponseWrapper(true, message, null));

    }

    public ResponseEntity<ResponseWrapper> showBookDetails(String bookTitle){

        if (!systemData.bookExists(bookTitle)) {
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Book book = systemData.findBook(bookTitle);
        message = "Book details retrieved successfully.\n";
        Map<String, Object> bookData = Map.of(
                "author", book.getAuthor(),
                "publisher", book.getPublisher(),
                "genres", book.getGenres(),
                "year", book.getPublicationYear(),
                "price", book.getPrice(),
                "averageRating", book.computeAverageRating()
        );
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, bookData));
    }

    public ResponseEntity<ResponseWrapper> showBookReviews (String bookTitle){
        if(!systemData.bookExists(bookTitle)){
            message = "This book doesn't exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Book book = systemData.findBook(bookTitle);

        ArrayList<Map<String,Object>> reviews = new ArrayList<>();
        for(Comment comment : book.getComments()){
            reviews.add(Map.of("username",comment.getUsername(),"rate",comment.getRating(),"comment",comment.getBody()));
        }
        double averageRate = book.computeAverageRating();
        message = "Book reviews retrieved successfully.\n";

        Map<String, Object> bookData = Map.of(
                "title", book.getTitle(),
                "reviews", reviews,
                "averageRating", averageRate
        );
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, bookData));
    }

    public ResponseEntity<ResponseWrapper> showBookContent(String username, String bookTitle){
        if (!systemData.userExists(username)) {
            message = "User doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!systemData.bookExists(bookTitle)) {
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if (!systemData.findUser(username).userHasAccessToBook(bookTitle)) {
            message = "You can't view this book!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        message = "Book Content retrieved successfully.";
        Book bookToBeShown = systemData.findBook(bookTitle);
        Map<String, Object> bookContent = Map.of(
                "title", bookTitle,
                "content", bookToBeShown.getContent()
        );
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, bookContent));

    }


}
