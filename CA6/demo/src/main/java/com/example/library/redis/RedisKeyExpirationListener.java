package com.example.library.redis;

import com.example.library.repository.Repository;          // <-- correct import
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener implements MessageListener {

    @Autowired
    private Repository systemData;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString(); // This is the token
        String[] parts = expiredKey.split(":");
        String username = parts[1];
        if (username != null) {
            systemData.loggedInUsers.remove(username);
            System.out.println("❌ Token expired. Removed user from loggedInUsers: " + username);
        } else {
            System.out.println("❌ Token expired: " + expiredKey);
            System.out.println("not username");
            systemData.loggedInUsers.remove("user2");
        }
    }
}
