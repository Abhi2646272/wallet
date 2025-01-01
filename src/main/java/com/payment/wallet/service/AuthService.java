package com.payment.wallet.service;

import com.payment.wallet.entity.User;
import com.payment.wallet.repo.UserRepository;
import com.payment.wallet.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
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
        LOGGER.info("Login attempt initiated.");

        // Log input data
        LOGGER.info("Received email: " + (email != null ? email : "null"));
        LOGGER.info("Received password: " + (password != null ? "[PROTECTED]" : "null"));

        // Validate input
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            LOGGER.warning("Email or password is blank.");
            throw new IllegalArgumentException("Email and password cannot be blank");
        }
        LOGGER.info("Validated email and password input.");

        // Find the user by email
        LOGGER.info("Searching for user with email: " + email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    LOGGER.warning("User not found for email: " + email);
                    return new RuntimeException("User not found");
                });
        LOGGER.info("User found: " + user);

        // Log user details (excluding sensitive data)
        LOGGER.info("User ID: " + user.getId());
        LOGGER.info("User Role: " + user.getRole());
        LOGGER.info("User Wallet ID: " + user.getWalletId());

        // Validate the password
        LOGGER.info("Validating password for user: " + email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            LOGGER.warning("Invalid credentials for email: " + email);
            throw new RuntimeException("Invalid credentials");
        }
        LOGGER.info("Password validated successfully for user: " + email);

        // Generate JWT token
        LOGGER.info("Generating JWT token for user: " + email);
        String token = jwtUtils.generateToken(user.getEmail(), user.getRole());
        LOGGER.info("JWT token generated successfully for user: " + email);

        // Return token
        LOGGER.info("Login process completed successfully for user: " + email);
        return token;
    }

    public String getUserRoleByEmail(String email) {
        // Retrieve the user from the repository
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Return the user's role as a string
        return user.getRole(); // Assuming `role` is an enum
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
}
