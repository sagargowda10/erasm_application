package com.erasm.utility;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JWTUtilTest {

    private JWTUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JWTUtil();
       
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "erasm-super-secret-key-that-is-long-enough-for-hs256-signing");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L);
    }

    @Test
    void generateToken_thenExtractEmail_roundTrips() {
        String token = jwtUtil.generateToken("sagar@erasm.com");

        assertNotNull(token);
        assertEquals("sagar@erasm.com", jwtUtil.extractEmail(token));
    }

    @Test
    void isTokenValid_freshToken_returnsTrue() {
        String token = jwtUtil.generateToken("sagar@erasm.com");

        assertTrue(jwtUtil.isTokenValid(token));
    }

    @Test
    void isTokenValid_garbageToken_returnsFalse() {
        assertFalse(jwtUtil.isTokenValid("not-a-real-token"));
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() {
       
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L);
        String expired = jwtUtil.generateToken("sagar@erasm.com");

        assertFalse(jwtUtil.isTokenValid(expired));
    }
}
