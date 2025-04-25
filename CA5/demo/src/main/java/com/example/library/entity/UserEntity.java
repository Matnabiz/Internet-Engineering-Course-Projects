package com.example.library.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    private String username;

    private String password;
    private String email;
    private String role;
    private int balance;

    @Embedded
    private AddressEmbeddable address;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getBalance() { return balance; }
    public void setBalance(int balance) { this.balance = balance; }

    public AddressEmbeddable getAddress() { return address; }
    public void setAddress(AddressEmbeddable address) { this.address = address; }
}
