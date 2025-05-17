package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "user_books")
public class UserBooksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_username", referencedColumnName = "username", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    private boolean onLoan;

    private Integer borrowDays; // Nullable, only relevant if onLoan == true

    private String startDate; // Nullable, only relevant if onLoan == true

    public UserBooksEntity() {}

    public UserBooksEntity(UserEntity user, BookEntity book, boolean onLoan, Integer loanDays, String startDate) {
        this.user = user;
        this.book = book;
        this.onLoan = onLoan;
        this.borrowDays = loanDays;
        this.startDate = startDate;
    }

    public void setUser(UserEntity user) { this.user = user; }

    public void setBook(BookEntity book) { this.book = book; }

    public void setOnLoan(boolean onLoan) { this.onLoan = onLoan; }

    public void setLoanDays(Integer loanDays) { this.borrowDays = loanDays; }

    public void setStartDate(String startDate) { this.startDate = startDate; }
}
