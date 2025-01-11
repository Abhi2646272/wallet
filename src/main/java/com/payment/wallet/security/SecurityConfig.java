package com.payment.wallet.security;

import com.payment.wallet.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.logging.Logger;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private static final Logger logger = Logger.getLogger(SecurityConfig.class.getName());

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring Security Filter Chain");

        http
                .cors(Customizer.withDefaults())

                .csrf((csrf) -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Public access (No authentication)
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN") // Only Admin
                        .requestMatchers("/api/v1/wallet/**").hasAnyRole("ADMIN", "USER") // Admin & User
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()  // Allow OPTIONS requests

                        .anyRequest().authenticated() // Other endpoints require authentication

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter
        logger.info("Security Filter Chain configured successfully");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt for secure password storage
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}




