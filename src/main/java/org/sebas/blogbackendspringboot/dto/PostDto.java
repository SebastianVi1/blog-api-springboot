package org.sebas.blogbackendspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebas.blogbackendspringboot.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    Long id;
    String title;
    String content;
    String authorName;
    LocalDateTime createdAt;
    String category;
    List<Comment> comments;

}
