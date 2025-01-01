package com.payment.wallet.service;

import com.payment.wallet.entity.User;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(String email, String password) {

        // Validate input
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
             throw new IllegalArgumentException("Email and password cannot be blank");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                     return new RuntimeException("User not found");
                });

        // Validate the password
         if (!passwordEncoder.matches(password, user.getPassword())) {
             throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());

        // Return token
         return token;
    }

    public String getUserRoleByEmail(String email) {
         User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
         return user.getRole();
    }

    public String refreshToken(String email, String password) {
        return jwtUtils.generateRefreshToken(email,password);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return jwtUtils.validateToken(refreshToken);
    }

    public String extractUsername(String refreshToken) {
        return jwtUtils.extractUsername(refreshToken);
    }

    public String generateToken(String email, String role) {
        return jwtUtils.generateToken(email,role);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }
}
