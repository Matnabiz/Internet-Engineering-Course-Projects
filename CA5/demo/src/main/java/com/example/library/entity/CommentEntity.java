package com.example.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "comments")

public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_username", referencedColumnName = "username", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    int rating;
    private String body;
    private LocalDateTime timestamp;

    public CommentEntity() {}

    public CommentEntity(UserEntity user, BookEntity book, int rating, String body) {
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.body = body;
    }

    @Override
    public String toString() {
        return this.user.getUsername() + " (" + this.rating+ ", " + this.timestamp + "): " + this.body;
    }

}
