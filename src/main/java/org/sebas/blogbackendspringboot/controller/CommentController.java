package org.sebas.blogbackendspringboot.controller;

import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.model.Comment;
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
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody Comment com){
        return service.addComment(postId, com);
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId){
        return service.getComments(postId);
    }
}
