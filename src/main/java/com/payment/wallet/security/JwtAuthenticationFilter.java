package com.payment.wallet.security;

import com.payment.wallet.entity.User;
import com.payment.wallet.service.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.logging.Logger;

@EnableWebSecurity
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = Logger.getLogger(JwtAuthenticationFilter.class.getName());


    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.info("JWT Token header: " + request.getHeaderNames());

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        logger.info("JWT Token header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            logger.info("JWT Token header: " + jwt);

            String username = jwtUtils.extractUsername(jwt);
            logger.info("Extracted role from username: " + username);

            String role = jwtUtils.extractRole(jwt); // Extract role from the token
            logger.info("Extracted role from token: " + role);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {


                if (jwtUtils.isTokenValid(jwt, username)) {
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);

                    // Using the builder pattern for UserDetails
                    UserDetails userDetails  =
                            org.springframework.security.core.userdetails.User.builder()
                                    .username(username)
                                    .password("")  // Not needed as JWT is stateless
                                    .authorities(Collections.singletonList(authority))
                                    .build();
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("SecurityContext set with authentication for user: " + username);
                    logger.info("User authorities: " + userDetails.getAuthorities());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
