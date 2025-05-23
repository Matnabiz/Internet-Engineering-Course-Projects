package com.example.library.config;

import com.example.library.entity.BookEntity;
import com.example.library.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BookDataLoader {

    private static final String API_URL = "http://194.60.231.242:8000/books";

    @Bean
    public CommandLineRunner loadBooks(BookRepository bookRepository) {
        return args -> {
            RestTemplate restTemplate = new RestTemplate();
            BookEntity[] books = restTemplate.getForObject(API_URL, BookEntity[].class);

            for (BookEntity book : books) {
                if (book.getGenres() != null) {
                    String joinedGenres = String.join(",", book.getGenres());
                    book.setGenres(joinedGenres);
                }
                bookRepository.save(book);
            }
            System.out.println("✅ Books imported successfully!");
        };
    }

}
