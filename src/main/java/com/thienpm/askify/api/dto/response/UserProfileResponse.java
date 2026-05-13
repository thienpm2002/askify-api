package com.thienpm.askify.api.dto.response;

import com.thienpm.askify.api.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private Integer id;
    private String userName;
    private String email;
    private String avatarUrl;
    private Role role;
}
