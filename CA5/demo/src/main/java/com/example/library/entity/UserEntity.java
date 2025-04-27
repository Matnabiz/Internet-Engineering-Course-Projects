package com.example.library.entity;
import lombok.*;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserEntity {
    @Id
    private String username;

    private String password;
    private String email;
    private String role;
    private int balance;

    @Embedded
    private AddressEmbeddable address;

}
