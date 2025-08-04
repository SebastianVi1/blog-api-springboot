package org.sebas.blogbackendspringboot.repository;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.dto.UpdatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.sebas.blogbackendspringboot.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {
    @Autowired
    private PostRepo postRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;

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
        userRepo.save(testUser);

        testCategory = new Category();
        testCategory.setName("Technology");
        categoryRepo.save(testCategory);

        testPost = new Post();
        testPost.setTitle("Test Post");
        testPost.setContent("Test content");
        testPost.setAuthor(testUser);
        testPost.setCategory(testCategory);
        testPost.setCreatedDate(LocalDateTime.now());
        testPost.setComments(List.of());

        createPostDto = new CreatePostDto("Test Post", "Test content", testUser.getId(), testCategory.getId());
        updatePostDto = new UpdatePostDto("Updated Post", "Updated content", testCategory.getId());
    }
    @Test
    void shouldFindByTitle() {
        // Given
        postRepository.save(testPost);

        // When
        List<Post> found = postRepository.searchByTitle("Test Post");

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.getFirst().getTitle()).isEqualTo("Test Post");
    }
}