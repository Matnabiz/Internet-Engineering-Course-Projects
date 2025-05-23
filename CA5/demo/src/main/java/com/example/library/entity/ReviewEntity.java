package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String title;
    private String comment;
    private String date;
    private int rate;

    public ReviewEntity() {
    }
    public ReviewEntity(String username, String title, String comment, int rate) {
        this.username = username;
        this.title = title;
        this.comment = comment;
        this.rate = rate;
        LocalDateTime now = LocalDateTime.now();
        this.date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));    }
}
