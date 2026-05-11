package com.themart.controller;

import com.themart.dto.AuthRequest;
import com.themart.dto.AuthResponse;


import com.themart.dto.RegisterRequest;
import com.themart.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.themart.repository.UserRepository;
// import com.themart.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    // private final UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
