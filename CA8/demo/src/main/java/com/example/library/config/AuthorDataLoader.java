package com.example.library.config;

import com.example.library.entity.AuthorEntity;
import com.example.library.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AuthorDataLoader {

    private static final String API_URL = "http://194.60.231.242:8000/authors";

    @Bean
    public CommandLineRunner loadAuthors(AuthorRepository authorRepository, RestTemplate restTemplate) {
        return args -> {
            AuthorEntity[] authors = restTemplate.getForObject(API_URL, AuthorEntity[].class);

            for (AuthorEntity author : authors) {
                if (!authorRepository.existsByNameAndPenName(author.getName(), author.getPenName())) {
                    authorRepository.save(author);
                }
            }
            System.out.println("✅ Authors imported successfully!");
        };
    }
}
