package com.thienpm.askify.api.exception;

import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.thienpm.askify.api.dto.response.ErrorResponse;
import com.thienpm.askify.api.enums.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Resource not found
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoResourceFoundException e) {
        return ResponseEntity
                .status(404)
                .body(ErrorResponse.of(ErrorCode.NOT_FOUND));
    }

    // Exception Valid Request
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest()
                .body(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, message));
    }

    // AppException (business)
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {

        ErrorResponse errorResponse = ErrorResponse.of(ex.getErrorCode());

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }

    // Exception Login(email, password)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity
                .status(401)
                .body(ErrorResponse.of(ErrorCode.INVALID_CREDENTIALS));
    }

    // loadUserById không tìm thấy user
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException e) {
        return ResponseEntity
                .status(401)
                .body(ErrorResponse.of(ErrorCode.INVALID_TOKEN));
    }

    // Refresh token hết hạn — phải trước JwtException!
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException e) {
        return ResponseEntity
                .status(401)
                .body(ErrorResponse.of(ErrorCode.TOKEN_EXPIRED));
    }

    // Token sai format, sai chữ ký...
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwt(JwtException e) {
        return ResponseEntity
                .status(401)
                .body(ErrorResponse.of(ErrorCode.INVALID_TOKEN));
    }

    // Exception chung
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());

        return ResponseEntity
                .internalServerError()
                .body(errorResponse);
    }
}
