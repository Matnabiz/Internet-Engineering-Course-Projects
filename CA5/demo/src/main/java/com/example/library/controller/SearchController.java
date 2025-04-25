package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/title/{searchedTitle}")
    public ResponseEntity<ResponseWrapper> searchBooksByTitle(@PathVariable String searchedTitle) {
        return searchService.searchBooksByTitle(searchedTitle);
    }

    @GetMapping("/author/{searchedAuthor}")
    public ResponseEntity<ResponseWrapper> searchBooksByAuthor(@PathVariable String searchedAuthor) {
        return searchService.searchBooksByAuthor(searchedAuthor);
    }

    @GetMapping("/genre/{searchedGenre}")
    public ResponseEntity<ResponseWrapper> searchBooksByGenre(@PathVariable String searchedGenre) {
        return searchService.searchBooksByGenre(searchedGenre);
    }

    @GetMapping("/year")
    public ResponseEntity<ResponseWrapper> searchBooksByYear(
            @RequestParam int fromYear,
            @RequestParam int toYear) {
        return searchService.searchBooksByYear(fromYear, toYear);
    }

    @GetMapping("/advanced")
    public ResponseEntity<ResponseWrapper> professionalSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false, defaultValue = "0") int fromYear,
            @RequestParam(required = false, defaultValue = "9999") int toYear,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {

        return searchService.professionalSearch(title, author, genre, fromYear, toYear, sortBy, sortOrder);
    }
}
