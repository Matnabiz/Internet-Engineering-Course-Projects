package com.example.library.repository;

import com.example.library.entity.BookEntity;
import com.example.library.entity.OrderEntity;
import com.example.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByUserUsername(String username);
    boolean existsByUserAndBook(UserEntity user, BookEntity book);
    @Query("SELECT new map(b.title as title, b.author as author, b.price as price, o.borrowed as borrowed, o.startDate as startDate, o.borrowDays as borrowDays) " +
            "FROM OrderEntity o JOIN o.book b JOIN o.user u WHERE u.username = :username")
    List<Map<String, Object>> retrieveUserBooks(@Param("username") String username);


}
