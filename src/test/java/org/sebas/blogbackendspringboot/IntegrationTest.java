package org.sebas.blogbackendspringboot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.CategoryRepo;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.sebas.blogbackendspringboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepo postRepo;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepo userRepo;
    private User testUser;
    private Category testCategory;
    private Post testPost;
    private CreatePostDto createPostDto;

    // Integration test for Post creation and setup
    // Sets up a test user, category, and post before each test
    @BeforeEach
    void setUp() {
        postRepo.deleteAll();
        // Create and save a test user
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpassword");
        testUser = userRepo.save(testUser);

        // Create and save a test category
        testCategory = new Category();
        testCategory.setName("Technology");
        testCategory = categoryRepo.save(testCategory);

        // Create and save a test post
        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCategory(testCategory);
        testPost.setCreatedDate(LocalDateTime.now());
        testPost.setComments(List.of());
        testPost = postRepo.save(testPost);

        // Prepare a DTO for post creation
        createPostDto = new CreatePostDto("Test Post Dto", "Test content...", testUser.getId(), testCategory.getId());
    }

    @Test
    void contextLoads() {}

    @Test
    // Test that a post can be created via the API with security enabled
    void shouldCreateAPost() throws Exception {
        postRepo.deleteAll();
        String json = objectMapper.writeValueAsString(createPostDto);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Post> posts = postRepo.findAll();

        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getTitle()).isEqualTo("Test Post Dto");
        assertThat(posts.getFirst().getId()).isNotNull();
        assertThat(posts.getFirst().getContent())
                .startsWith("T")
                .isEqualToIgnoringCase("Test content...");
    }

    @Test
    // Test that all posts are returned from the API
    void shouldReturnAllPosts() throws Exception {
        postRepo.save(testPost);
        assertThat(postRepo.findAll()).hasSize(1);
        mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Post"));
    }

    @Test
    // Test that a post can be deleted via the API
    void shouldDeleteAPost() throws Exception {
        assertThat(postRepo.findAll())
            .extracting(Post::getId)
            .contains(testPost.getId());
        mockMvc.perform(delete("/api/posts/{id}", testPost.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk());

        assertThat(postRepo.findAll()).hasSize(0);
    }

    @Test
    // Test that a post can be retrieved by its ID via the API
    void shouldReturnAPostDtoFromId() throws Exception {
        assertThat(postRepo.findAll()).extracting(post -> post.getId())
                        .contains(testPost.getId());
        mockMvc.perform(get("/api/posts/{id}", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    // Test that an existing post can be updated via the API
    void shouldUpdateAExistentPost() throws Exception {
        mockMvc.perform(put("/api/posts/{id}", testPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(createPostDto))
                .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Test Post Dto"));
    }

    @Test
    // Test that searching posts by title returns the correct list
    void shouldReturnASearchedPostList() throws Exception {
        assertThat(postRepo.findAll()).hasSizeGreaterThan(0);
        mockMvc.perform(get("/api/posts/search")
                        .param("title","test post")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user("testuser").password("testpassword").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Post"));
    }
}
