package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.entity.BookEntity;
import com.example.library.entity.CommentEntity;
import com.example.library.entity.UserEntity;
import com.example.library.model.Book;
import com.example.library.model.Comment;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    private final Repository systemData;
    private String message;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AuthorRepository authorRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    private final CommentRepository commentRepository;

    public BookService(Repository systemData,
                       AuthorRepository authorRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository,
                       OrderRepository userBooksRepository,
                       CommentRepository commentRepository){
        this.systemData = systemData;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = userBooksRepository;
        this.authorRepository = authorRepository;
        this.commentRepository = commentRepository;
    }

    public ResponseEntity<ResponseWrapper> addBook (String username, String bookTitle,
                                                    String bookAuthor, String bookPublisher,
                                                    int publishYear, ArrayList<String> bookGenres,
                                                    String bookContent, String bookSynopsis, int bookPrice){

        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!userRepository.existsByUsername(username)){
            message = "This username doesn't exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(bookRepository.existsByTitle(bookTitle)){
            message = "This book already exist in system!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        if(!authorRepository.existsByName(bookAuthor)){
            message = "The author of this book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        UserEntity userAddingBook = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));;

        if (!userAddingBook.getRole().equals("admin")) {
            message = "Only an admin can add books!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        if(!Validation.minimumGenreCount(bookGenres)){
            message = "A Book should at least have one genre!" ;
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        Book newBook = new Book(bookTitle, bookAuthor,
                bookPublisher, publishYear,
                bookGenres,bookPrice,bookSynopsis,bookContent);
        BookEntity bookToBeAdded = new BookEntity(
                userAddingBook.getUsername() ,bookTitle, bookAuthor, bookPublisher,
                publishYear, bookPrice, bookSynopsis, bookContent);
        bookToBeAdded.setGenresFromList(bookGenres);

        bookRepository.save(bookToBeAdded);
        message =  "Book added successfully.";
        return ResponseEntity.ok(new ResponseWrapper(true, message, null));

    }

    public ResponseEntity<ResponseWrapper> showBookDetails(String bookTitle){

        if (!bookRepository.existsByTitle(bookTitle)) {
            message = "Book doesn't exist!";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }

        BookEntity book = bookRepository.findByTitle(bookTitle).orElseThrow(() -> new RuntimeException("Book not found."));

        message = "Book details retrieved successfully.\n";
        Map<String, Object> bookData = Map.of(
                "author", book.getAuthor(),
                "publisher", book.getPublisher(),
                "genres", book.getGenres(),
                "year", book.getYear(),
                "price", book.getPrice(),
                "reviews", this.gatherBookReviews(bookTitle),
                "averageRating", computeAverageRating(book)
        );
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, bookData));
    }

    public ArrayList<Map<String,Object>> gatherBookReviews (String bookTitle){

        Book book = systemData.findBook(bookTitle);
        ArrayList<Map<String,Object>> reviews = new ArrayList<>();
        for(Comment comment : book.getComments()){
            reviews.add(Map.of("username",comment.getUsername(),"rate",comment.getRating(),"comment",comment.getBody()));
        }
        return  reviews;
    }

    public ResponseEntity<ResponseWrapper> showBookContent(String username, String bookTitle){
        if (!systemData.isLoggedIn(username)) {
            message = "Unauthorized: You should log into your account in order to access this.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
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

    public double computeAverageRating(BookEntity book){
        List<CommentEntity> comments = commentRepository.findByBook(book);

        if (comments.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (CommentEntity comment : comments) {
            totalRating += comment.getRating();
        }

        return (double) totalRating / comments.size();
    }


}
