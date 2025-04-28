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

    @JsonSetter("genres")
    public void setGenresFromList(List<String> genresList) {
        this.genres = String.join(",", genresList);
    }
}
