package com.thienpm.askify.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSearchRequest {
    @Size(max = 100)
    private String keyword;

    @Min(value = 0, message = "Page must be at least 0")
    private int page = 0;

    @Min(value = 1, message = "Size must be at least 1")
    @Max(value = 50, message = "Size must be at most 50")
    private int size = 10;

    @Pattern(regexp = "createdAt|voteCount|title", message = "SortBy must be 'createdAt' or 'voteCount' or 'title'")
    private String sortBy = "createdAt";

    @Pattern(regexp = "asc|desc", message = "SortDir must be 'asc' or 'desc'")
    private String sortDir = "desc";
}
