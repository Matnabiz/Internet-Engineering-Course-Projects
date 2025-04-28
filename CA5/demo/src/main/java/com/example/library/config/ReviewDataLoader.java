package com.example.library.config;

import com.example.library.entity.ReviewEntity;
import com.example.library.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ReviewDataLoader {

    private static final String API_URL = "http://194.60.231.242:8000/reviews";

    @Bean
    public CommandLineRunner loadReviews(ReviewRepository reviewRepository, RestTemplate restTemplate) {
        return args -> {
            ReviewEntity[] reviews = restTemplate.getForObject(API_URL, ReviewEntity[].class);

            for (ReviewEntity review : reviews) {
                reviewRepository.save(review);
            }
            System.out.println("✅ Reviews imported successfully!");
        };
    }
}
