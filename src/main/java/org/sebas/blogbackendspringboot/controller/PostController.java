package org.sebas.blogbackendspringboot.controller;

import jakarta.validation.Valid;
import org.sebas.blogbackendspringboot.dto.CreatePostDto;
import org.sebas.blogbackendspringboot.dto.PostDto;
import org.sebas.blogbackendspringboot.dto.UpdatePostDto;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "/api")
public class PostController {

    private PostService service;

    @Autowired
    public void setService(PostService service){
        this.service = service;
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPostsList(){
        return service.getPostsList();
    }

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostDto createPostDto){
        return service.createPost(createPostDto);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id){
        return service.getPostById(id);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody UpdatePostDto updatePostDto){
        return service.updatePost(id, updatePostDto);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id){
        return service.deletePost(id);
    }

    /**
     * Search posts by title
     * Returns posts matching the title parameter
     */
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@RequestParam String title){
        return service.searchPostByTitle(title);
    }

    /**
     * Get posts by author ID
     * Returns all posts written by the specified author
     */
    @GetMapping("/posts/author/{id}")
    public ResponseEntity<List<PostDto>> searchPostByAuthor(@PathVariable Long id){
        return service.searchPostByAuthorId(id);
    }
}
