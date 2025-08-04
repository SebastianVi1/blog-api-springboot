package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.dto.PostDto;
import org.sebas.blogbackendspringboot.dto.UpdatePostDto;
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
     * Convert Post entity to PostDto for secure data transfer
     */
    private PostDto createPostDto(Post post) {
        List<CommentDto> commentDtos = post.getComments().stream()
                .map(comment -> {
                    CommentDto dto = new CommentDto();
                    dto.setId(comment.getId());
                    dto.setContent(comment.getContent());
                    dto.setCreatedAt(comment.getCreatedAt());
                    dto.setAuthor(comment.getUser() != null ? comment.getUser().getUsername() : null);
                    dto.setPostId(post.getId());
                    return dto;
                })
                .toList();
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getUsername(),
                post.getAuthor().getId(),
                post.getCreatedDate(),
                post.getCategory().getName(),
                commentDtos
        );
    }

    //Return a new list with dtos
    private List<PostDto> convertToDtoList(List<Post> postList){
        return postList.stream().map(this::createPostDto).toList();
    }

    public ResponseEntity<List<PostDto>> getPostsList() {
        List<Post> posts = repo.findAll();
        List<PostDto> postDtoList = convertToDtoList(posts);
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

    public ResponseEntity<PostDto> getPostById(Long id){
        Optional<Post> post = repo.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        PostDto postDto = createPostDto(post.get());
        return ResponseEntity.ok(postDto);
    }

    /**
     * Update an existing post
     * Validates post existence and category before updating
     */
    public ResponseEntity<?> updatePost(Long id, UpdatePostDto updatePostDto) {
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
        return ResponseEntity.ok().build();
    }

    /**
     * Search posts by title
     * Returns posts matching the title parameter
     */
    public ResponseEntity<List<PostDto>> searchPostByTitle(String title) {
        List<Post> posts = repo.searchByTitle(title);
        List<PostDto> postDtoList = convertToDtoList(posts);
        return ResponseEntity.ok(postDtoList);
    }

    /**
     * Get posts by author ID
     * Returns all posts written by the specified author
     */
    public ResponseEntity<List<PostDto>> searchPostByAuthorId(Long id){
        List<Post> postList = repo.searchPostByAuthorId(id);
        List<PostDto> postDtoList = convertToDtoList(postList);
        return new ResponseEntity<>(postDtoList, HttpStatus.OK);
    }
}
