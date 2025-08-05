package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.model.Category;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.CategoryRepo;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.sebas.blogbackendspringboot.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepo repo;
    private UserRepo userRepo;
    private CategoryRepo categoryRepo;

    @Autowired
    public void setRepo(PostRepo repo, UserRepo userRepo, CategoryRepo categoryRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.categoryRepo = categoryRepo;
    }

    /**
     * Convert Post entity to CreatePostDto for secure data transfer
     */
    private CreatePostDto createCreatePostDto(Post post) {
        return new CreatePostDto(
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getId(),
                post.getCategory().getId()
        );
    }

    //Return a new list with dtos
    private List<CreatePostDto> convertToCreatePostDtoList(List<Post> postList){
        return postList.stream().map(this::createCreatePostDto).toList();
    }

    public ResponseEntity<List<CreatePostDto>> getPostsList() {
        List<Post> posts = repo.findAll();
        List<CreatePostDto> postDtoList = convertToCreatePostDtoList(posts);
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }

    /**
     * Create a new post
     * Validates author and category existence before creating post
     */
    public ResponseEntity<?> createPost(CreatePostDto createPostDto) {
        Optional<User> author = userRepo.findById(createPostDto.getAuthorId());
        Optional<Category> category = categoryRepo.findById(createPostDto.getCategoryId());
        
        if (author.isEmpty()) {
            return ResponseEntity.badRequest().body("Author not found");
        }
        
        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("Category not found");
        }
        
        Post post = new Post();
        post.setTitle(createPostDto.getTitle());
        post.setContent(createPostDto.getContent());
        post.setAuthor(author.get());
        post.setCategory(category.get());
        post.setCreatedDate(LocalDateTime.now());
        
        repo.save(post);
        return ResponseEntity.ok(createPostDto);
    }

    public ResponseEntity<CreatePostDto> getPostById(Long id){
        Optional<Post> post = repo.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CreatePostDto postDto = createCreatePostDto(post.get());
        return ResponseEntity.ok(postDto);
    }

    /**
     * Update an existing post
     * Validates post existence and category before updating
     */
    public ResponseEntity<?> updatePost(Long id, CreatePostDto updatePostDto) {
        Optional<Post> existingPost = repo.findById(id);
        Optional<Category> category = categoryRepo.findById(updatePostDto.getCategoryId());

        if (existingPost.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        
        if (category.isEmpty()) {
            return ResponseEntity.badRequest().body("Category not found");
        }
        
        Post post = existingPost.get();
        post.setTitle(updatePostDto.getTitle());
        post.setContent(updatePostDto.getContent());
        post.setCategory(category.get());
        
        repo.save(post);
        return ResponseEntity.ok(post);
    }

    public ResponseEntity<?> deletePost(Long id){
        Optional<Post> post = repo.findById(id);

        if (post.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        repo.delete(post.get());
        return ResponseEntity.ok(post);
    }

    /**
     * Search posts by title
     * Returns posts matching the title parameter
     */
    public ResponseEntity<List<CreatePostDto>> searchPostByTitle(String title) {
        List<Post> posts = repo.searchByTitle(title);
        List<CreatePostDto> postDtoList = convertToCreatePostDtoList(posts);
        return ResponseEntity.ok(postDtoList);
    }

    /**
     * Get posts by author ID
     * Returns all posts written by the specified author
     */
    public ResponseEntity<List<CreatePostDto>> searchPostByAuthorId(Long id){
        List<Post> postList = repo.searchPostByAuthorId(id);
        List<CreatePostDto> postDtoList = convertToCreatePostDtoList(postList);
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }
}
