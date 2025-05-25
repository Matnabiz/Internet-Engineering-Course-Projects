package com.example.library.controller;

import com.example.library.dto.ResponseWrapper;
import com.example.library.security.JwtUtil;
import com.example.library.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtUtil jwtUtil;
    private final AuthenticationService authenticationService;
    public AuthenticationController(JwtUtil jwtUtil, AuthenticationService authenticationService) {
        this.jwtUtil = jwtUtil;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody Map<String, Object> body) {


        return authenticationService.loginUser((String) body.get("username"), (String) body.get("password"));
    }
}