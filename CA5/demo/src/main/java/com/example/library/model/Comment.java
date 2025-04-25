package com.example.library.model;
import java.time.LocalDateTime;

public class Comment {
    private String username;
    int rating;
    private String body;
    private LocalDateTime timestamp;

    public Comment(String username, String body, int rating) {
        this.username = username;
        this.rating = rating;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }

    public String getUsername() { return this.username; }

    public int getRating() { return this.rating; }

    public String getBody() { return this.body; }

    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return this.username + " (" + this.rating+ ", " + this.timestamp + "): " + this.body;
    }
}
