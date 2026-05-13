package com.thienpm.askify.api.exception;

import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.thienpm.askify.api.dto.response.ErrorResponse;
import com.thienpm.askify.api.enums.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        // Resource not found
        @ExceptionHandler(NoResourceFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFound(
                        NoResourceFoundException e,
                        HttpServletRequest request) {

                log.warn("RESOURCE_NOT_FOUND path={}", request.getRequestURI());

                return ResponseEntity
                                .status(404)
                                .body(ErrorResponse.of(ErrorCode.NOT_FOUND));
        }

        // Http method sai
        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        public ResponseEntity<ErrorResponse> handleMethodNotAllowed(
                        HttpRequestMethodNotSupportedException e,
                        HttpServletRequest request) {

                log.warn("METHOD_NOT_ALLOWED path={} method={}",
                                request.getRequestURI(),
                                request.getMethod());

                return ResponseEntity
                                .status(404)
                                .body(ErrorResponse.of(ErrorCode.NOT_FOUND));
        }

        // Validation body
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(
                        MethodArgumentNotValidException e,
                        HttpServletRequest request) {

                String message = e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.joining(", "));

                log.warn("VALIDATION_FAILED path={} message={}",
                                request.getRequestURI(),
                                message);

                return ResponseEntity.badRequest()
                                .body(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, message));
        }

        // Validation query param
        @ExceptionHandler(BindException.class)
        public ResponseEntity<ErrorResponse> handleBindException(
                        BindException e,
                        HttpServletRequest request) {

                String message = e.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .collect(Collectors.joining(", "));

                log.warn("BIND_EXCEPTION path={} message={}",
                                request.getRequestURI(),
                                message);

                return ResponseEntity.badRequest()
                                .body(ErrorResponse.of(ErrorCode.VALIDATION_FAILED, message));
        }

        // Business exception
        @ExceptionHandler(AppException.class)
        public ResponseEntity<ErrorResponse> handleAppException(
                        AppException e,
                        HttpServletRequest request) {

                log.warn("APP_EXCEPTION path={} errorCode={}",
                                request.getRequestURI(),
                                e.getErrorCode());

                ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());

                return ResponseEntity
                                .status(errorResponse.getStatus())
                                .body(errorResponse);
        }

        // Login fail
        @ExceptionHandler(BadCredentialsException.class)
        public ResponseEntity<ErrorResponse> handleBadCredentials(
                        BadCredentialsException e,
                        HttpServletRequest request) {

                log.warn("AUTH_LOGIN_FAILED path={}", request.getRequestURI());

                return ResponseEntity
                                .status(401)
                                .body(ErrorResponse.of(ErrorCode.INVALID_CREDENTIALS));
        }

        // User not found
        @ExceptionHandler(UsernameNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleUsernameNotFound(
                        UsernameNotFoundException e,
                        HttpServletRequest request) {

                log.warn("AUTH_USER_NOT_FOUND path={}", request.getRequestURI());

                return ResponseEntity
                                .status(401)
                                .body(ErrorResponse.of(ErrorCode.INVALID_TOKEN));
        }

        // Token expired
        @ExceptionHandler(ExpiredJwtException.class)
        public ResponseEntity<ErrorResponse> handleExpiredJwt(
                        ExpiredJwtException e,
                        HttpServletRequest request) {

                log.warn("JWT_EXPIRED path={}", request.getRequestURI());

                return ResponseEntity
                                .status(401)
                                .body(ErrorResponse.of(ErrorCode.TOKEN_EXPIRED));
        }

        // Token invalid
        @ExceptionHandler(JwtException.class)
        public ResponseEntity<ErrorResponse> handleJwt(
                        JwtException e,
                        HttpServletRequest request) {

                log.warn("JWT_INVALID path={}", request.getRequestURI());

                return ResponseEntity
                                .status(401)
                                .body(ErrorResponse.of(ErrorCode.INVALID_TOKEN));
        }

        // System error
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleException(
                        Exception e,
                        HttpServletRequest request) {

                log.error("SYSTEM_ERROR path={}", request.getRequestURI(), e);

                return ResponseEntity
                                .internalServerError()
                                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
        }
}