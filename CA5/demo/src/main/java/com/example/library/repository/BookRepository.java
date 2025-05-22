package com.example.library.repository;

import com.example.library.entity.BookEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {
    Optional<BookEntity> findByTitle(String title);
    boolean existsByTitle(String title);


    List<BookEntity> findByTitleContainingIgnoreCase(String bookTitle);

    List<BookEntity> findByAuthorContainingIgnoreCase(String author);

    List<BookEntity> findByGenresContainingIgnoreCase(String genre);

    List<BookEntity> findByYearBetween(int fromYear, int toYear);

    List<BookEntity> findAll(Specification<BookEntity> spec, Sort sort);
}
