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
    public UserEntity() {}
    public UserEntity(String username, String password, String email, String role, int balance, AddressEmbeddable address) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.balance = balance;
        this.address = address;
    }
}
