package com.example.library.service;

import com.example.library.dto.ResponseWrapper;
import com.example.library.model.Book;
import com.example.library.repository.Repository;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchService {

    private final Repository systemData;
    private String message;

    public SearchService(Repository systemData){ this.systemData = systemData; }

    public ResponseEntity<ResponseWrapper> searchBooksByTitle(String bookTitle){

        ArrayList<Book> searchedBook = (ArrayList<Book>) systemData.books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(bookTitle.toLowerCase()))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", bookTitle,
                "books",fBooks
        );
        message = "Books containing '" + bookTitle + "'  in their title:";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }

    public ResponseEntity<ResponseWrapper> searchBooksByAuthor(String authorSearchQuery){

        ArrayList<Book> searchedBook = (ArrayList<Book>) systemData.books.stream()
                .filter(book -> book.getAuthor().trim().toLowerCase().contains(authorSearchQuery.toLowerCase()))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", authorSearchQuery,
                "books",fBooks
        );
        message = "Books by " + authorSearchQuery;
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }

    public ResponseEntity<ResponseWrapper> searchBooksByGenre (String genre){
        ArrayList<Book> searchedBook = (ArrayList<Book>) systemData.books.stream()
                .filter(book -> book.getGenres().contains(genre))
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", genre,
                "books",fBooks
        );
        message = "Books in the '" + genre + "' genre:";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }

    public ResponseEntity<ResponseWrapper> searchBooksByYear (int fromYear,int toYear){
        ArrayList<Book> searchedBook = (ArrayList<Book>) systemData.books.stream()
                .filter(book -> book.getPublicationYear() >= fromYear && book.getPublicationYear() <= toYear)
                .collect(Collectors.toList());

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "search", fromYear+"-"+toYear,
                "books",fBooks
        );
        message = "Books published from '"+fromYear+"' to '"+toYear+"':";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }

    public ResponseEntity<ResponseWrapper> professionalSearch(String searchTitle,String searchAuthor,String searchGenre,
                                                              int searchFromYear,int searchEndYear,String arrangeResultBy,String arrangeMode){

        ArrayList<Book> searchedBook = new ArrayList<Book>();

        if(searchTitle!=null) {
            searchedBook = (ArrayList<Book>) systemData.books.stream()
                    .filter(book -> book.getTitle().trim().toLowerCase().contains(searchTitle.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(searchAuthor!=null) {
            searchedBook = (ArrayList<Book>) searchedBook.stream()
                    .filter(book -> book.getAuthor().trim().toLowerCase().contains(searchAuthor.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if(searchGenre!=null) {
            searchedBook = (ArrayList<Book>) searchedBook.stream()
                    .filter(book -> book.getGenres().contains(searchGenre))
                    .collect(Collectors.toList());
        }
        if(searchFromYear!=0){
            searchedBook = (ArrayList<Book>) searchedBook.stream()
                    .filter(book -> book.getPublicationYear() >= searchFromYear && book.getPublicationYear() <= searchEndYear)
                    .collect(Collectors.toList());
        }

        if(arrangeResultBy!=null){
            if(arrangeResultBy == "avergeRate"){
                if(arrangeMode=="HighToLow") {
                    ArrayList<Book> sortedBooks = new ArrayList<>(searchedBook);
                    sortedBooks.sort(Comparator.comparingDouble(Book::getAverageRate).reversed());
                } else if (arrangeMode=="LowToHigh") {
                    ArrayList<Book> sortedBooks = new ArrayList<>(searchedBook);
                    sortedBooks.sort(Comparator.comparingDouble(Book::getAverageRate));
                }
            }
            if(arrangeResultBy == "NumberOfComments"){
                if(arrangeMode=="HighToLow") {
                    ArrayList<Book> sortedBooks = new ArrayList<>(searchedBook);
                    sortedBooks.sort(Comparator.comparingDouble(Book::getNumberOfComments).reversed());
                } else if (arrangeMode=="LowToHigh") {
                    ArrayList<Book> sortedBooks = new ArrayList<>(searchedBook);
                    sortedBooks.sort(Comparator.comparingDouble(Book::getNumberOfComments));
                }
            }
        }

        ArrayList<Map<String,Object>> fBooks = new ArrayList<>();
        for(Book book : searchedBook){
            fBooks.add(Map.of("title",book.getTitle(),
                    "author",book.getAuthor(),
                    "publisher",book.getPublisher(),
                    "genres",book.getGenres(),
                    "year",book.getPublicationYear(),
                    "price",book.getPrice(),
                    "synopsis",book.getSynopsis()));
        }

        Map<String, Object> searchResult = Map.of(
                "books",fBooks
        );
        message = "searched Success";
        return ResponseEntity.ok().body(new ResponseWrapper(true, message, searchResult));
    }
    

}
