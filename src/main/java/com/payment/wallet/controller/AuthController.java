package com.payment.wallet.controller;

import com.payment.wallet.dto.UserRegisterDTO;
import com.payment.wallet.entity.User;
import com.payment.wallet.security.JwtUtils;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.service.AuthService;
import com.payment.wallet.service.UserService;
import jakarta.validation.Valid;
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

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterDTO userDTO) {

        return ResponseEntity.ok(userService.registerUser(userDTO));
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String accessToken = authService.login(credentials.getOrDefault("email","").trim(), credentials.getOrDefault("password","").trim());

            String role = authService.getUserRoleByEmail(credentials.getOrDefault("email","").trim());
//            String refreshToken = authService.refreshToken(credentials.getOrDefault("email","").trim(),credentials.getOrDefault("password","").trim());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "role", role,
                    "accessToken", accessToken


            ));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", ex.getMessage()
            ));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        if (authService.validateRefreshToken(refreshToken)) {
            String email = authService.extractUsername(refreshToken);
            String newAccessToken = authService.generateToken(email, "USER"); // Adjust role as needed
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "accessToken", newAccessToken
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of(
                    "status", "error",
                    "message", "Invalid refresh token"
            ));
        }
    }

}
