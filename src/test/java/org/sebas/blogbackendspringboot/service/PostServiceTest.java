package org.sebas.blogbackendspringboot.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.CategoryRepo;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepo postRepo;

    @Mock
    UserRepo userRepo;

    @Mock
    CategoryRepo categoryRepo;

    @InjectMocks
    PostService postService;

    private User testUser;
    private Category testCategory;
    private Post testPost;
    private CreatePostDto createPostDto;

    @BeforeEach
    void setUp(){
        // Setup test data
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Technology");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCategory(testCategory);
        testPost.setCreatedDate(LocalDateTime.now());
        testPost.setComments(Arrays.asList());

        createPostDto = new CreatePostDto("Test Post", "Test content", 1L, 1L);
    }
    @Test
    void shouldCreateAPost(){
        when(userRepo.findById(1L)).thenReturn(Optional.of(testUser));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(testCategory));
        when(postRepo.save(any(Post.class))).thenReturn(testPost);

        // When
        ResponseEntity<?> response = postService.createPost(createPostDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userRepo).findById(1L);
        verify(categoryRepo).findById(1L);
        verify(postRepo).save(any(Post.class));


    }

    @Test
    void shouldReturnListWithTasks(){
        // Given
        when(postRepo.findAll()).thenReturn(List.of(testPost));

        // THen
        ResponseEntity<List<CreatePostDto>> postList = postService.getPostsList();

        //Then
        assertNotNull(postList.getBody());
        assertThat(postList.getBody().size()).isEqualTo(1);

        assertThat(postList.getBody().getFirst().getContent())
                .isEqualToIgnoringCase("test content");

        verify(postRepo).findAll();

    }

    @Test
    void shouldReturnAPostById(){
        // Given
        when(postRepo.findById(1L)).thenReturn(Optional.of(testPost));
        // When
        ResponseEntity<CreatePostDto> result = postService.getPostById(1L);
        //Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(result.getBody());
        assertThat(result.getBody().getTitle()).isEqualTo("Test Post");
        verify(postRepo).findById(1L);
    }

    @Test
    void shouldDeleteATaskWithOkStatus(){
        //Given
        when(postRepo.findById(1L)).thenReturn(Optional.of(testPost));
        //When
        ResponseEntity<?> result = postService.deletePost(1L);
        //Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(postRepo).findById(1L);
    }

    @Test
    void shouldUpdateAPostWithOkStatus(){
        //Given
        when(postRepo.findById(1L)).thenReturn(Optional.of(testPost));
        when(categoryRepo.findById(1L)).thenReturn(Optional.of(testCategory));
        //When
        var result = postService.updatePost(1L,createPostDto);
        //Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(postRepo).findById(1L);
        verify(categoryRepo).findById(1L);
    }

    @Test
    void souldReturnAPostByHisTitleWithStatusOK(){
       //Given
        when(postRepo.searchByTitle("Test Post")).thenReturn(List.of(testPost));

        //When
        ResponseEntity<List<CreatePostDto>> result = postService.searchPostByTitle("Test Post");
        //THen
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(result.getBody());
        assertThat(result.getBody().size()).isEqualTo(1);
        assertThat(result.getBody().getFirst().getTitle())
                .isEqualTo("Test Post");

        verify(postRepo).searchByTitle("Test Post");
    }
}