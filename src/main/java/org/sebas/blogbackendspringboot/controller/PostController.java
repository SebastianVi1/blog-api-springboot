package org.sebas.blogbackendspringboot.controller;

import org.sebas.blogbackendspringboot.dto.PostDto;
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
    public ResponseEntity<?> postPost(@RequestBody Post post){
        return service.postPost(post);

    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id){
        return service.getPostByid(id);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> editPost (@PathVariable Long id){
        return service.editPost(id);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost (@PathVariable Long id){
        return service.deletePost(id);
    }


    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> searchPostByTitle(@RequestParam String title){
        System.out.println("Searching with " + title);
        return service.searchPostByTitle(title);
    }

    @GetMapping("/posts/author/{id}")
    public ResponseEntity<List<PostDto>> searchPostByAuthor(@PathVariable Long id){
        return service.searchPostByAuthorId(id);
    }
}
