package com.thienpm.askify.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponse {
    private Integer id;
    private String title;
    private String content;
    private Integer voteCount;
    private List<String> tags;
    private AuthorQuestionResponse author;
    private LocalDateTime createdAt;
}
