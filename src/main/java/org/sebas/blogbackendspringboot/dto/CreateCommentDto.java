package org.sebas.blogbackendspringboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentDto {
    
    @NotBlank(message = "Comment content is required")
    @Size(min = 1, max = 800, message = "Comment must be between 1 and 800 characters")
    private String content;
    
    @NotNull(message = "User ID is required")
    private Long userId;
} 