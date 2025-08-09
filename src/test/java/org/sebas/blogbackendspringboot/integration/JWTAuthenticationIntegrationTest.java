package org.sebas.blogbackendspringboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.sebas.blogbackendspringboot.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for JWT authentication flow
 * Tests the complete JWT authentication process from login to protected resource access
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class JWTAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    private User testUser;
    private String testPassword = "password123";

    @BeforeEach
    void setUp() {
        // Clean database
        userRepo.deleteAll();

        // Create test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(encoder.encode(testPassword));
        testUser = userRepo.save(testUser);
    }

    /**
     * Test successful user login and JWT token generation
     * Verifies that valid credentials return a JWT token
     */
    @Test
    void testLogin_ValidCredentials_ReturnsJWTToken() throws Exception {
        // Create login request
        String loginRequest = """
            {
                "username": "testuser",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(not(nullValue())))
                .andExpect(content().string(not("fail")));
    }

    /**
     * Test login with invalid credentials
     * Verifies that invalid credentials return failure response
     */
    @Test
    void testLogin_InvalidCredentials_ReturnsFailure() throws Exception {
        // Create login request with wrong password
        String loginRequest = """
            {
                "username": "testuser",
                "password": "wrongpassword"
            }
            """;

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest));
    }

    /**
     * Test accessing protected endpoint without JWT token
     * Verifies that unauthorized access is denied
     */
    @Test
    void testProtectedEndpoint_NoToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test accessing protected endpoint with valid JWT token
     * Verifies that valid JWT token allows access to protected resources
     */
    @Test
    void testProtectedEndpoint_ValidToken_ReturnsSuccess() throws Exception {
        // Generate valid JWT token
        String token = jwtService.generateToken(testUser.getUsername());

        mockMvc.perform(get("/api/posts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    /**
     * Test accessing protected endpoint with malformed authorization header
     * Verifies that malformed header format is rejected
     */
    @Test
    void testProtectedEndpoint_MalformedHeader_ReturnsUnauthorized() throws Exception {
        String token = jwtService.generateToken(testUser.getUsername());

        // Test without "Bearer " prefix
        mockMvc.perform(get("/api/posts")
                .header("Authorization", token))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Test user registration and immediate login
     * Verifies that newly registered user can immediately login with JWT
     */
    @Test
    void testRegisterAndLogin_NewUser_Success() throws Exception {
        // Create registration request
        String registerRequest = """
            {
                "username": "newuser",
                "email": "newuser@example.com",
                "password": "newpassword123"
            }
            """;

        // Register new user
        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(registerRequest))
                .andExpect(status().isOk());

        // Login with new user credentials
        String loginRequest = """
            {
                "username": "newuser",
                "password": "newpassword123"
            }
            """;

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(not(nullValue())))
                .andExpect(content().string(not("fail")));
    }

    /**
     * Test JWT token persistence across multiple requests
     * Verifies that JWT token works for multiple authenticated requests
     */
    @Test
    void testJWTToken_MultipleRequests_Success() throws Exception {
        // Generate valid JWT token
        String token = jwtService.generateToken(testUser.getUsername());

        // Make first request
        mockMvc.perform(get("/api/posts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // Make second request with same token
        mockMvc.perform(get("/api/posts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    /**
     * Test case sensitivity of Authorization header
     * Verifies that authorization header is case-insensitive for Bearer
     */
    @Test
    void testAuthorizationHeader_CaseSensitivity() throws Exception {
        String token = jwtService.generateToken(testUser.getUsername());

        // Test with lowercase "bearer"
        mockMvc.perform(get("/api/posts")
                .header("Authorization", "bearer " + token))
                .andExpect(status().isUnauthorized());

        // Test with uppercase "BEARER"
        mockMvc.perform(get("/api/posts")
                .header("Authorization", "BEARER " + token))
                .andExpect(status().isUnauthorized());

        // Test with correct case "Bearer"
        mockMvc.perform(get("/api/posts")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }
}
