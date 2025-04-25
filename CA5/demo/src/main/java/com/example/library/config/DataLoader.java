package com.example.library.config;

import com.example.library.entity.AddressEmbeddable;
import com.example.library.entity.UserEntity;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DataLoader {

    private static final String API_URL = "http://194.60.231.242:8000/users";

    @Bean
    public CommandLineRunner loadData(UserRepository userRepository) {
        return args -> {
            RestTemplate restTemplate = new RestTemplate();
            UserEntity[] users = restTemplate.getForObject(API_URL, UserEntity[].class);

            if (users != null) {
                for (UserEntity user : users) {
                    // Prevent duplicates if re-run
                    if (!userRepository.existsById(user.getUsername())) {
                        userRepository.save(user);
                    }
                }
                System.out.println("✅ Users imported successfully!");
            } else {
                System.err.println("⚠️ No users found at API endpoint.");
            }
        };
    }
}
