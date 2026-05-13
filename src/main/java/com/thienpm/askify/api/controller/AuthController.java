package com.thienpm.askify.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.config.JwtProperties;
import com.thienpm.askify.api.dto.request.LoginRequestDTO;
import com.thienpm.askify.api.dto.request.RegisterRequestDTO;
import com.thienpm.askify.api.dto.response.AuthResponse;
import com.thienpm.askify.api.dto.response.AuthResult;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.auth.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtProperties jwtProperties;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody @Valid RegisterRequestDTO request,
            HttpServletResponse response) {

        log.info("AUTH_REGISTER_REQUEST email=", request.getEmail());

        AuthResult authResult = authService.register(request);

        addRefreshTokenCookie(response, authResult.getRefreshToken());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(authResult.getAccessToken())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody @Valid LoginRequestDTO request,
            HttpServletResponse response) {

        log.info("AUTH_LOGIN_REQUEST email={}", request.getEmail());

        AuthResult authResult = authService.login(request);

        addRefreshTokenCookie(response, authResult.getRefreshToken());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(authResult.getAccessToken())
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {

        // Đọc refresh token từ cookie
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        // Validate + generate token mới → để AuthService xử lý
        AuthResult authResult = authService.refreshToken(refreshToken);

        // Set refresh token mới vào cookie (rotation)
        addRefreshTokenCookie(response, authResult.getRefreshToken());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(authResult.getAccessToken())
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Integer userId = userDetails.getUser().getId();

        // Xóa refresh token cookie bằng cách set Max-Age = 0
        String cookie = "refreshToken=; HttpOnly; Secure; SameSite=Strict; Path=api/v1/auth; Max-Age=0";
        response.addHeader("Set-Cookie", cookie);

        // Clear SecurityContext
        SecurityContextHolder.clearContext();

        log.info("AUTH_LOGOUT_SUCCESS userId={}", userId);

        return ResponseEntity.noContent().build();
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        String cookie = String.format(
                "refreshToken=%s; HttpOnly; Secure; SameSite=Strict; Path=api/v1/auth; Max-Age=%d",
                refreshToken,
                jwtProperties.getRefreshTokenExpiration());
        response.addHeader("Set-Cookie", cookie);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
