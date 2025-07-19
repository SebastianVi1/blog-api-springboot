package org.sebas.blogbackendspringboot.controller;

import jakarta.validation.Valid;
import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreateCommentDto;
import org.sebas.blogbackendspringboot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    public CommentService service;

    @Autowired
    public void setService(CommentService service){
        this.service = service;
    }
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @Valid @RequestBody CreateCommentDto createCommentDto){
        return service.addComment(postId, createCommentDto);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId){
        return service.getComments(postId);
    }
}
