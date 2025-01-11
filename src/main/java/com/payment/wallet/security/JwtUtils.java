package com.payment.wallet.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.InvalidKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    
    private final String SECRET = "Your32ByteLongSecretKeyThatIsSecureEnoughForHS256";    // should define in env file

    public String generateToken(String username, String role) {
        try {
            logger.info("Generating token for username:" + username + "with role: "+  role);

             String token = Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
//                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                    .signWith(SignatureAlgorithm.HS256, SECRET)
                    .compact();
             logger.info("token------ "+token);
            return token;
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }
    public String generateRefreshToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }

    public boolean isTokenValid(String token, String username) {
        return extractUsername(token).equals(username);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired: " + ex.getMessage());
        } catch (JwtException ex) {
            System.out.println("Invalid token: " + ex.getMessage());
        }
        return false;
    }
}
