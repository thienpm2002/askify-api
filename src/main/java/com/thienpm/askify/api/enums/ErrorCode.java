package com.thienpm.askify.api.enums;

public enum ErrorCode {
    // Auth
    TOKEN_EXPIRED("Token has expired", 401),
    INVALID_TOKEN("Invalid token", 401),
    UNAUTHORIZED("Unauthorized", 401),
    FORBIDDEN("Access denied", 403),

    // User
    EMAIL_ALREADY_EXISTS("Email already exists", 409),
    INVALID_CREDENTIALS("Email or password is incorrect", 401),

    // Common
    NOT_FOUND("Resource not found", 404),
    VALIDATION_FAILED("Validation failed", 400),
    INTERNAL_SERVER_ERROR("Internal server error", 500);

    private final String message;
    private final int httpStatus;

    ErrorCode(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}
