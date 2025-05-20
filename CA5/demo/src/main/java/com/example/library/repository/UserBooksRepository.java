package com.example.library.repository;

import com.example.library.entity.BookEntity;
import com.example.library.entity.OrderEntity;
import com.example.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBooksRepository extends JpaRepository<OrderEntity, String> {
    static List<OrderEntity> findByUserUsername(String username) {
        return null;
    }

    boolean existsByUserAndBook(UserEntity user, BookEntity book);
}
