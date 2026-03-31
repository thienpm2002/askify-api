package com.thienpm.askify.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthorQuestionResponse {
    private Integer id;
    private String userName;
    private String avatarUrl;
}
