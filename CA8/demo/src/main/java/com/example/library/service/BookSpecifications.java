package com.example.library.service;
import com.example.library.entity.BookEntity;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<BookEntity> titleContains(String title) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<BookEntity> authorContains(String author) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("author")), "%" + author.toLowerCase() + "%");
    }

    public static Specification<BookEntity> genreEquals(String genre) {
        return (root, query, cb) ->
                cb.isMember(genre, root.get("genres"));
    }

    public static Specification<BookEntity> yearBetween(int fromYear, int toYear) {
        return (root, query, cb) ->
                cb.between(root.get("year"), fromYear, toYear);
    }
}
