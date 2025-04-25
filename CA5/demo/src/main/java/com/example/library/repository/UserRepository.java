package com.example.library.repository;

import com.example.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    // You can define custom queries here if needed
}
