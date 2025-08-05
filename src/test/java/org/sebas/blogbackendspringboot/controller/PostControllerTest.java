package org.sebas.blogbackendspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.TestSecurityConfig;
import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.service.JWTService;
import org.sebas.blogbackendspringboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    }

    @Test
    void shouldReturnOkRequestingAllPosts() throws Exception {
        List<CommentDto> comments = List.of();
        //Given
        List<CreatePostDto> posts = List.of(
                        new CreatePostDto("Test Title", "Test Content", 1L, 1L),
                        new CreatePostDto("Second Title", "More Content", 2L, 2L)
                );

        //When and then
        when(postService.getPostsList()).thenReturn(ResponseEntity.ok(posts));
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
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

    @Test
    void shouldReturnOkWithAnArrayWithCreatePostDto() throws Exception {
        CreatePostDto createPostDto = new CreatePostDto();
        createPostDto.setTitle("Test title");
        List<CreatePostDto> listCreatePostDto = new ArrayList<>();
        listCreatePostDto.add(createPostDto);
        when(postService.searchPostByTitle("Test")).thenReturn(ResponseEntity.ok(listCreatePostDto));
        mockMvc.perform(get("/api/posts/search")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].title").value("Test title"));
    }

}
