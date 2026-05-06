package com.thienpm.askify.api.dto.projection;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerFlatDTO {
    private Integer id;
    private String content;
    private Integer voteCount;
    private boolean accepted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer userId;
    private String userName;
    private String avatarUrl;
}
