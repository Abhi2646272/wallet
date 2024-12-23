package com.payment.wallet.controller;

import com.payment.wallet.entity.User;
import com.payment.wallet.security.JwtUtils;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.AuthProvider;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String token = authService.login(credentials.get("email"), credentials.get("password"));
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "token", token
            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", ex.getMessage()
            ));
        }
    }

}
