package com.example.library.repository;

import com.example.library.entity.BookEntity;
import com.example.library.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}
