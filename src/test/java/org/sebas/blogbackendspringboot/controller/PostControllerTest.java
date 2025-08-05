package org.sebas.blogbackendspringboot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.SecurityConfig.SecurityConfig;
import org.sebas.blogbackendspringboot.TestSecurityConfig;
import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.dto.PostDto;
import org.sebas.blogbackendspringboot.dto.UpdatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Comment;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.CategoryRepo;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.sebas.blogbackendspringboot.service.JWTService;
import org.sebas.blogbackendspringboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@Import({TestSecurityConfig.class})
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Category testCategory;
    private Post testPost;
    private CreatePostDto createPostDto;
    private UpdatePostDto updatePostDto;
    @BeforeEach
    void setUp(){
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("testpassword");

        testCategory = new Category();
        testCategory.setName("Technology");

        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCategory(testCategory);
        testPost.setCreatedDate(LocalDateTime.now());
        testPost.setComments(List.of());

        createPostDto = new CreatePostDto("Test Post", "Test content", 1L, 1L);
        updatePostDto = new UpdatePostDto("Updated Post", "Updated content", 1L);
    }

    @Test
    void shouldReturnOkRequestingAllPosts() throws Exception {
        List<CommentDto> comments = List.of();
        //Given
        List<PostDto> posts = List.of(
                new PostDto(1L, "Test Title", "Test Content", "John Doe", 1L,
                        LocalDateTime.now(), "Tech", comments),
                new PostDto(2L, "Second Title", "More Content", "Jane Doe", 2L,
                        LocalDateTime.now(), "Science", comments)
        );

        //When and then
        when(postService.getPostsList()).thenReturn(ResponseEntity.ok(posts));
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Title"));
    }

    @Test
    void shouldreturnCode200CreatingAPost() throws Exception {
        String json = objectMapper.writeValueAsString(createPostDto);
       mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
               .andExpect(status().isOk());

    }
    @Test
    void shouldReturnNotFoundForInvalidId() throws Exception {
        when(postService.getPostById(999L)).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(get("/api/posts/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidPost() throws Exception {
        CreatePostDto invalidPost = new CreatePostDto(); // empty post

        mockMvc.perform(post("/api/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPost)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnOkDeletingAPost() throws Exception {
        mockMvc.perform(delete("/api/posts/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNoFoundWithInvalidParam() throws Exception {
        when(postService.deletePost(999909L)).thenReturn(ResponseEntity.notFound().build());
        mockMvc.perform(delete("/api/posts/{id}", 999909L))
                .andExpect(status().isNotFound());
    }

}
