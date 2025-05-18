package com.example.library.repository;

import com.example.library.entity.BookEntity;
import com.example.library.entity.UserBooksEntity;
import com.example.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserBooksRepository extends JpaRepository<UserBooksEntity, String> {
    static List<UserBooksEntity> findByUserUsername(String username) {
        return null;
    }

    boolean existsByUserAndBook(UserEntity user, BookEntity book);
}
