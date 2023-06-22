package com.netology.diploma.controller;

import com.netology.diploma.dto.auth.AuthRequest;
import com.netology.diploma.dto.auth.AuthResponse;
import com.netology.diploma.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(HttpServletRequest http, @RequestBody AuthRequest authRequest) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String token = authService.login(http, authRequest);
        return new ResponseEntity<>(new AuthResponse(token), HttpStatus.OK);
    }

    @GetMapping("login")
    public ResponseEntity<?> login() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("auth-token") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Success logout");
    }
}
