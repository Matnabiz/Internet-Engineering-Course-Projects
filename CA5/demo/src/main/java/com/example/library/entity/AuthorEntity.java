package com.example.library.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "authors")
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String penName;
    private String nationality;
    private LocalDate born;
    private LocalDate died; // Optional

    // Getters and Setters
}
