package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String bookTitle;
    private String comment;
    private String date;
    private int rating;


    public ReviewEntity(UserEntity customer, BookEntity book, int rating, String commentBody) {
    }
}
