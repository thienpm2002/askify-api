package com.thienpm.askify.api.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerResponse {
    private Integer id;
    private String content;
    private Integer voteCount;
    private AuthorResponse author;
    private Integer questionId;
    private boolean accepted;
    private LocalDateTime createdAt;
}
