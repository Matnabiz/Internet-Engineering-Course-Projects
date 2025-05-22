package com.example.library.repository;

import com.example.library.entity.AuthorEntity;
import com.example.library.entity.BookEntity;
import com.example.library.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBook(BookEntity book);
}
