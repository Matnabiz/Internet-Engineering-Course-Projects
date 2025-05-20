package com.example.library.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_username", referencedColumnName = "username", nullable = false)
     private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookEntity book;

    private boolean borrowed;

    private Integer borrowDays; // Nullable, only relevant if onLoan == true

    private String startDate; // Nullable, only relevant if onLoan == true

    public OrderEntity() {}

    public OrderEntity(UserEntity user, BookEntity book, boolean borrowed, Integer loanDays, String startDate) {
        this.user = user;
        this.book = book;
        this.borrowed = borrowed;
        this.borrowDays = loanDays;
        this.startDate = startDate;
    }
}
