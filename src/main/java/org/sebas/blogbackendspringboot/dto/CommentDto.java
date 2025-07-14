package org.sebas.blogbackendspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sebas.blogbackendspringboot.model.Post;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String author;
    private Post post;
}
