package com.example.library.repository;

import com.example.library.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> findByRole(String role);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<UserEntity> findByUsernameContaining(String partialUsername);
    void deleteByEmail(String email);
}
