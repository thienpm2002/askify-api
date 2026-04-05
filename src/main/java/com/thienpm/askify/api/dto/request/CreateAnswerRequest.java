package com.thienpm.askify.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAnswerRequest {
    @NotBlank(message = "Content is required")
    private String content;
    @NotNull(message = "Question ID is required")
    @Min(value = 1, message = "Question ID must be a positive integer")
    private Integer questionId;
}
