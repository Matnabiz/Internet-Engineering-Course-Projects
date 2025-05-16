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

    public AuthorEntity(String username, String name, String penName,
                        String nationality, LocalDate born, LocalDate died) {
        this.username = username;
        this.name = name;
        this.penName = penName;
        this.nationality = nationality;
        this.born = born;
        this.died = died;
    }
}