package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Book;
import com.example.library.entity.BookEntity;
import com.example.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class SearchService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BookRepository bookRepository;
    @Autowired
    private final ReviewRepository reviewRepository;
    @Autowired
    private final OrderRepository orderRepository;
    @Autowired
    public SearchService(
            UserRepository userRepository,
            BookRepository bookRepository,
            ReviewRepository reviewRepository,
            OrderRepository orderRepository,
            Repository systemData) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }


    public ResponseEntity<ResponseWrapper> searchBooksByTitle(String bookTitle) {
        List<BookEntity> books = bookRepository.findByTitleContainingIgnoreCase(bookTitle);
        List<Map<String, Object>> fBooks = this.bookListToHashMap(books);
        Map<String, Object> searchResult = Map.of(
                "search", bookTitle,
                "books", fBooks
        );
        String message = "Books containing '" + bookTitle + "' in their title:";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }
    public ResponseEntity<ResponseWrapper> searchBooksByAuthor(String author) {
        List<BookEntity> books = bookRepository.findByAuthorContainingIgnoreCase(author);
        List<Map<String, Object>> fBooks = this.bookListToHashMap(books);
        Map<String, Object> searchResult = Map.of(
                "search", author,
                "books", fBooks
        );
        String message = "Books by " + author;
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }
    public ResponseEntity<ResponseWrapper> searchBooksByGenre(String genre) {
        List<BookEntity> books = bookRepository.findByGenresContainingIgnoreCase(genre);
        List<Map<String, Object>> fBooks = this.bookListToHashMap(books);
        Map<String, Object> searchResult = Map.of(
                "search", genre,
                "books", fBooks
        );
        String message = "Books in the '" + genre + "' genre:";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }
    public ResponseEntity<ResponseWrapper> searchBooksByYear(int fromYear, int toYear) {
        List<BookEntity> books = bookRepository.findByYearBetween(fromYear, toYear);
        List<Map<String, Object>> fBooks = this.bookListToHashMap(books);
        Map<String, Object> searchResult = Map.of(
                "search", fromYear + "-" + toYear,
                "books", fBooks
        );

        String message = "Books published from '" + fromYear + "' to '" + toYear + "':";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }
    public ResponseEntity<ResponseWrapper> professionalSearch(String searchTitle,
                                                              String searchAuthor,
                                                              String searchGenre,
                                                              int searchFromYear,
                                                              int searchEndYear,
                                                              String arrangeResultBy,
                                                              String arrangeMode) {

        Specification<BookEntity> spec = Specification.where(null);

        if (searchTitle != null && !searchTitle.isBlank()) {
            spec = spec.and(BookSpecifications.titleContains(searchTitle));
        }

        if (searchAuthor != null && !searchAuthor.isBlank()) {
            spec = spec.and(BookSpecifications.authorContains(searchAuthor));
        }

        if (searchGenre != null && !searchGenre.isBlank()) {
            spec = spec.and(BookSpecifications.genreEquals(searchGenre));
        }

        if (searchFromYear != 0 && searchEndYear != 0) {
            spec = spec.and(BookSpecifications.yearBetween(searchFromYear, searchEndYear));
        }

        Sort sort = Sort.unsorted();

        if ("averageRate".equals(arrangeResultBy)) {
            sort = Sort.by("averageRate");
        } else if ("numberOfComments".equals(arrangeResultBy)) {
            sort = Sort.by("numberOfComments");
        }

        if ("HighToLow".equals(arrangeMode)) {
            sort = sort.descending();
        } else if ("LowToHigh".equals(arrangeMode)) {
            sort = sort.ascending();
        }

        List<BookEntity> results = bookRepository.findAll(spec, sort);
        List<Map<String, Object>> fBooks = this.bookListToHashMap(results);
        String message = "Search successful";
        return ResponseEntity.ok(new ResponseWrapper(true, message, fBooks));
    }
    private List<Map<String, Object>> bookListToHashMap(List<BookEntity> books){
        return books.stream()
                .map(book -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", book.getTitle());
                    map.put("author", book.getAuthor());
                    map.put("publisher", book.getPublisher());
                    map.put("genres", book.getGenres());
                    map.put("year", book.getYear());
                    map.put("price", book.getPrice());
                    map.put("synopsis", book.getSynopsis());
                    return map;
                })
                .toList();
    }


}
