package org.sebas.blogbackendspringboot.service;

import org.sebas.blogbackendspringboot.dto.CommentDto;
import org.sebas.blogbackendspringboot.dto.CreateCommentDto;
import org.sebas.blogbackendspringboot.model.Comment;
import org.sebas.blogbackendspringboot.model.Post;
import org.sebas.blogbackendspringboot.model.User;
import org.sebas.blogbackendspringboot.repo.CommentRepo;
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
public class CommentService {
    private CommentRepo commentRepo;
    private PostRepo postRepo;
    private UserRepo userRepo;
    
    @Autowired
    private void setCommentService(PostRepo postRepo, CommentRepo commentRepo, UserRepo userRepo){
        this.postRepo = postRepo;
        this.commentRepo = commentRepo;
        this.userRepo = userRepo;
    }

     //Convert Comment entity to CommentDto for secure data transfer
    private CommentDto createDto(Comment comment){
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUser().getUsername(),
                comment.getPost().getId()
        );
    }

    public ResponseEntity<?> addComment(Long postId, CreateCommentDto createCommentDto) {
        Optional<Post> postOptional = postRepo.findById(postId);
        Optional<User> userOptional = userRepo.findById(createCommentDto.getUserId());

        if (postOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post not found");
        }

        if (userOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }

        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setUser(userOptional.get());
        comment.setPost(postOptional.get());
        comment.setCreatedAt(LocalDateTime.now());

        commentRepo.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    public ResponseEntity<List<CommentDto>> getComments(Long postId) {
        List<Comment> comments = commentRepo.findByPostId(postId);
        List<CommentDto> commentDtos = comments.stream().map(this::createDto).toList();
        return new ResponseEntity<>(commentDtos, HttpStatus.OK);
    }
}
