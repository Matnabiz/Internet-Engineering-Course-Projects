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
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public void setDied(LocalDate died) {
        this.died = died;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPenName() {
        return penName;
    }

    public String getNationality() {
        return nationality;
    }

    public LocalDate getBorn() {
        return born;
    }

    public LocalDate getDied() {
        return died;
    }
}
