package com.thienpm.askify.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileResponse {
    private Integer id;
    private String userName;
    private String email;
    private String avatarUrl;
}
