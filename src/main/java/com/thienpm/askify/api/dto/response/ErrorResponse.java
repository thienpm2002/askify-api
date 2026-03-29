package com.thienpm.askify.api.dto.response;

import java.time.LocalDateTime;

import com.thienpm.askify.api.enums.ErrorCode;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private final String error;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;

    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .error(errorCode.name())
                .message(errorCode.getMessage())
                .status(errorCode.getHttpStatus())
                .timestamp(LocalDateTime.now())
                .build();
    }

}
