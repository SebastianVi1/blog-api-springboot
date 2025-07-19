package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.model.Comment;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.repo.CommentRepo;
import org.sebas.blogbackendspringboot.repo.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private CommentRepo commentRepo;
    private PostRepo postRepo;
    @Autowired
    private void setCommentService(PostRepo postRepo, CommentRepo commentRepo){
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
    }

    private CommentDto createDto(Comment com){
        return new CommentDto(
                com.getId(),
                com.getContent(),
                com.getCreatedAt(),
                com.getUser().getUsername(),
                com.getPost()
        );
    }
    public ResponseEntity<?> addComment(Long postId, Comment com) {
         Optional<Post> postOptional = postRepo.findById(postId);

         if (postOptional.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body("Post not found");

         }
         Post post = postOptional.get();
         commentRepo.save(com);

         return ResponseEntity.status(HttpStatus.CREATED).body(com);
    }

    public ResponseEntity<List<CommentDto>> getComments(Long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(this::createDto).toList();
        return new ResponseEntity<List<CommentDto>>(commentDtos, HttpStatus.OK);
    }
}
