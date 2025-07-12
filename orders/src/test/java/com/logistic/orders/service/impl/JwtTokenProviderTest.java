package com.logistic.orders.service.impl;
import com.logistic.orders.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();

        ReflectionTestUtils.setField(jwtTokenProvider, "secret", "MiClaveSuperSecretaParaJwtQueDebeTenerMuchosBytes123!");
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 10000L);
    }

    @Test
    void generateToken_shouldReturnValidToken() {
        String token = jwtTokenProvider.generateToken("admin");

        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals("admin", jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        String invalidToken = "esto.no.es.un.jwt";
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }

    @Test
    void validateToken_shouldReturnFalseForExpiredToken() throws InterruptedException {
        JwtTokenProvider shortExpiryProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(shortExpiryProvider, "secret", "MiClaveSuperSecretaParaJwtQueDebeTenerMuchosBytes123!");
        ReflectionTestUtils.setField(shortExpiryProvider, "expiration", 500L);
        String token = shortExpiryProvider.generateToken("admin");
        Thread.sleep(600);
        assertFalse(shortExpiryProvider.validateToken(token));
    }

    @Test
    void getUsernameFromToken_shouldReturnSubject() {
        String token = jwtTokenProvider.generateToken("admin");
        String username = jwtTokenProvider.getUsernameFromToken(token);

        assertEquals("admin", username);
    }
}