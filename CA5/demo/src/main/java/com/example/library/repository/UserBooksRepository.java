package com.example.library.repository;

import com.example.library.entity.UserBooksEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBooksRepository extends JpaRepository<UserBooksEntity, String> {
    // You can define custom queries here if needed
}
