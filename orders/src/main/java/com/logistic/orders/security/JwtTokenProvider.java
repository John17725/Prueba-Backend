package com.logistic.orders.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username) {
        logger.info("Generating token for user: {}", username);
        String tokenGenerated = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("Token generated: {}", tokenGenerated);
        return tokenGenerated;
    }

    public String getUsernameFromToken(String token) {
        logger.info("getting username from token: {}", token);
        String username = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        logger.debug("username: {}", username);
        return username;
    }

    public boolean validateToken(String token) {
        logger.info("validating token: {}", token);
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            logger.info("token validated: {}", token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
