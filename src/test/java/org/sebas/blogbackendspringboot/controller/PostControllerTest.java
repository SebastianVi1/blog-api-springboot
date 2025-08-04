package org.sebas.blogbackendspringboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.SecurityConfig.SecurityConfig;
import org.sebas.blogbackendspringboot.TestSecurityConfig;
import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.PostDto;
import org.sebas.blogbackendspringboot.model.Comment;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


}
