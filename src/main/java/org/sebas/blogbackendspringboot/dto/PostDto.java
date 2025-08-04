package org.sebas.blogbackendspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private Long authorId;
    private LocalDateTime createdAt;
    private String category;
    private List<CommentDto> comments;

    public PostDto(Long id, @NotBlank(message = "Title is required") @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters") String title, @NotBlank(message = "Content is required") @Size(min = 10, message = "Content must be at least 10 characters") String content, @NotBlank(message = "Username is required") @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") String username, LocalDateTime createdDate, @NotBlank(message = "Category name is required") @Size(min = 2, max = 50, message = "Category name must be between 2 and 50 characters") String name, List<CommentDto> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorName = username;
        this.authorId = authorId;
        this.createdAt = createdDate;
        this.category = name;
        this.comments = comments;
    }
}

