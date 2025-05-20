package com.example.library.entity;

import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String title;
    private String author;
    private String publisher;
    private int year;
    private int price;
    private String synopsis;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String genres;

    public BookEntity() {}

    public BookEntity
            (String username, String title, String author,
             String publisher, int year, int price,
             String synopsis, String content) {
        this.username = username;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.price = price;
        this.synopsis = synopsis;
        this.content = content;
    }

    @JsonSetter("genres")
    public void setGenresFromList(List<String> genresList) {
        this.genres = String.join(",", genresList);
    }
}
