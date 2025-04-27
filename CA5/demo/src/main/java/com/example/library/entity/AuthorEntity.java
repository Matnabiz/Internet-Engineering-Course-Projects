package com.example.library.entity;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class AuthorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String penName;
    private String nationality;
    private LocalDate born;
    private LocalDate died;
}