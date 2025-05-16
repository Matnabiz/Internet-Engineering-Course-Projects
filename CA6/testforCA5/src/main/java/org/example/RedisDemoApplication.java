package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import jakarta.annotation.PostConstruct;

//import javax.annotation.PostConstruct;

@SpringBootApplication
class RedisDemoApplication {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForValue().set("hello", "world");
            String value = redisTemplate.opsForValue().get("hello");
            System.out.println("✅ Redis set/get successful: " + value);
        } catch (Exception e) {
            System.err.println("❌ Redis connection failed: " + e.getMessage());
        }
    }
}
