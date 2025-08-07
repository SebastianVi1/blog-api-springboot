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
import static org.hamcrest.Matchers.isIn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(
        addFilters = false )// Disable security filters for testing)
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
    @BeforeEach
    void setUp() {
        postRepo.deleteAll();
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpassword");
        testUser = userRepo.save(testUser);

        testCategory = new Category();
        testCategory.setName("Technology");
        testCategory = categoryRepo.save(testCategory);

        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCategory(testCategory);
        testPost.setCreatedDate(LocalDateTime.now());
        testPost.setComments(List.of());

        createPostDto = new CreatePostDto("Test Post", "Test content",testUser.getId(),testCategory.getId());
    }
    @Test
    void contextLoads() {}

    @Test
    void shouldCreateAPost() throws Exception {
        String json = objectMapper.writeValueAsString(createPostDto);

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        List<Post> posts = postRepo.findAll();

        assertThat(posts).hasSize(1);
        assertThat(posts.getFirst().getTitle()).isEqualTo("Test Post");
        assertThat(posts.getFirst().getId()).isNotNull();
        assertThat(posts.getFirst().getContent())
                .startsWith("T")
                .isEqualToIgnoringCase("Test content");
    }

    @Test
    void shoulReturnAllTasks() throws Exception {
        postRepo.save(testPost);
        assertThat(postRepo.findAll()).hasSize(1);
        mockMvc.perform(get("/api/posts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Post"));
    }

    @Test
    void shouldDeleteAPost() throws Exception {
        Post savedPost = postRepo.save(testPost);
        assertThat(postRepo.findAll())
            .extracting(Post::getId)
            .contains(savedPost.getId());
        mockMvc.perform(delete("/api/posts/{id}",savedPost.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(postRepo.findAll()).hasSize(0);
    }

    @Test
    void sohuldReturnAPostDtoFromId() throws Exception {
        testPost = postRepo.save(testPost);
        assertThat(postRepo.findAll()).extracting(post -> post.getId())
                        .contains(testPost.getId());
        mockMvc.perform(get("/api/posts/{id}", testPost.getId() )
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));

    }

}
