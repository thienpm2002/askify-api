package com.thienpm.askify.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
}
