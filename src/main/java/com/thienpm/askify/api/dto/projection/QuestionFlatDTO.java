package com.thienpm.askify.api.dto.projection;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionFlatDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer voteCount;
    private LocalDateTime createdAt;
    private Integer userId;
    private String userName;
    private String avatarUrl;
}
