package org.sebas.blogbackendspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 800, message = "Comment must be between 1 and 800 characters")
    private String content;

    private LocalDateTime createdAt;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String author;

    Long postId;
}