package com.example.library.repository;

import com.example.library.entity.AuthorEntity;
import com.example.library.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByNameAndPenName(String name, String penName);
    boolean existsByName(String authorName);
    Optional<AuthorEntity> findByName(String name);

}
