package org.sebas.blogbackendspringboot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.model.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for JWTService
 * Tests JWT token generation, validation, and claims extraction
 */
@ExtendWith(MockitoExtension.class)
class JWTServiceTest {

    @InjectMocks
    private JWTService jwtService;

    private UserDetails userDetails;
    private String testUsername;
    private String validToken;

    @BeforeEach
    void setUp() throws Exception {
        // Create test user
        testUsername = "testuser";
        User user = new User();
        user.setUsername(testUsername);
        user.setPassword("password123");
        user.setEmail("test@example.com");
        userDetails = new UserPrincipal(user);

        // Initialize JWT service (simulates constructor)
        jwtService = new JWTService();

        // Generate a valid token for testing
        validToken = jwtService.generateToken(testUsername);
    }

    /**
     * Test successful JWT token generation
     * Verifies that token is not null or empty
     */
    @Test
    void testGenerateToken_Success() {
        String token = jwtService.generateToken(testUsername);

        assertNotNull(token, "Token should not be null");
        assertFalse(token.isEmpty(), "Token should not be empty");
        assertTrue(token.startsWith("eyJ"), "Token should start with JWT header");
    }

    /**
     * Test username extraction from JWT token
     * Verifies that the correct username is extracted from the token
     */
    @Test
    void testExtractUsername_Success() {
        String extractedUsername = jwtService.extractUsername(validToken);

        assertEquals(testUsername, extractedUsername,
            "Extracted username should match the original username");
    }

    /**
     * Test JWT token validation with valid token and matching user
     * Verifies that validation returns true for valid scenarios
     */
    @Test
    void testValidateToken_ValidToken_ReturnsTrue() {
        boolean isValid = jwtService.validateToken(validToken, userDetails);

        assertTrue(isValid, "Token should be valid for matching user");
    }

    /**
     * Test JWT token validation with mismatched username
     * Verifies that validation returns false when usernames don't match
     */
    @Test
    void testValidateToken_MismatchedUsername_ReturnsFalse() {
        // Create different user
        User differentUser = new User();
        differentUser.setUsername("differentuser");
        differentUser.setPassword("password123");
        UserDetails differentUserDetails = new UserPrincipal(differentUser);

        boolean isValid = jwtService.validateToken(validToken, differentUserDetails);

        assertFalse(isValid, "Token should be invalid for different user");
    }

    /**
     * Test token structure and format
     * Verifies that generated token has correct JWT structure
     */
    @Test
    void testTokenStructure() {
        String token = jwtService.generateToken(testUsername);

        // JWT tokens have 3 parts separated by dots
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length, "JWT token should have 3 parts");

        // Each part should be base64 encoded (not empty)
        for (String part : tokenParts) {
            assertFalse(part.isEmpty(), "Token parts should not be empty");
        }
    }



    /**
     * Test token with null username
     * Verifies behavior when null username is provided
     */
    @Test
    void testGenerateToken_NullUsername() {
        assertDoesNotThrow(() -> {
            String token = jwtService.generateToken(null);
            assertNotNull(token, "Token should be generated even with null username");
        }, "Should handle null username gracefully");
    }

    /**
     * Test extracting username from invalid token format
     * Verifies that invalid token throws appropriate exception
     */
    @Test
    void testExtractUsername_InvalidToken() {
        String invalidToken = "invalid.token.format";

        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(invalidToken);
        }, "Invalid token should throw exception");
    }
    /**
     * Helper method to create an expired token for testing
     * Uses reflection to access private methods and create expired token
     */
    private String createExpiredToken() throws Exception {
        // Get the secret key from the service
        Field secretKeyField = JWTService.class.getDeclaredField("secretkey");
        secretKeyField.setAccessible(true);
        String secretKey = (String) secretKeyField.get(jwtService);

        // Create an expired token manually
        Date expiredDate = new Date(System.currentTimeMillis() - 1000); // 1 second ago

        return Jwts.builder()
                .subject(testUsername)
                .issuedAt(new Date(System.currentTimeMillis() - 2000))
                .expiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
                .compact();
    }
}
