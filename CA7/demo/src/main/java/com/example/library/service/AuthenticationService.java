package com.example.library.service;


import com.example.library.dto.ResponseWrapper;
import com.example.library.entity.UserEntity;
import com.example.library.repository.Repository;
import com.example.library.repository.UserRepository;
import com.example.library.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private final UserRepository userRepository;
    private final Repository systemData;
    private String message;
    private final JwtUtil jwtUtil;


    public AuthenticationService(UserRepository userRepository, Repository systemData, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.systemData = systemData;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<ResponseWrapper> loginUser(String username, String password) {
        if (!userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ResponseWrapper(false, "User not found!", null));
        }

        if (systemData.isLoggedIn(username)) {
            message = "This user has already signed in.";
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, message, null));
        }
        UserEntity userLoggingIn = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        if (!Validation.authenticatePassword(password, userLoggingIn)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseWrapper(false, "Invalid credentials.", null));
        }

        systemData.loggedInUsers.add(username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", username);
        claims.put("role", "USER");
        String token = jwtUtil.generateToken(claims);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(new ResponseWrapper(true, "Login Successful", response));
    }

}
