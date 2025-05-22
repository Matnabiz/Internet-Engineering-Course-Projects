package com.example.library.repository;

import com.example.library.entity.BookEntity;
import com.example.library.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {

    List<ReviewEntity> findByBookTitle(String bookTitle);


}
