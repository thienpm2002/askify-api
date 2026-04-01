package com.thienpm.askify.api.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQuestionRequest {
    @NotBlank(message = "Title is required")
    @Size(max = 255)
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Tags is required")
    @Size(min = 1, max = 5, message = "Tags must be between 1 and 5")
    private List<String> tags;
}
