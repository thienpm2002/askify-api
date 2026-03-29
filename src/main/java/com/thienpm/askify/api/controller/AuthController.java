package com.thienpm.askify.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.config.JwtProperties;
import com.thienpm.askify.api.dto.request.LoginRequestDTO;
import com.thienpm.askify.api.dto.request.RegisterRequestDTO;
import com.thienpm.askify.api.dto.response.AuthResponse;
import com.thienpm.askify.api.dto.response.AuthResult;
import com.thienpm.askify.api.service.auth.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

        AuthResult authResult = authService.login(request);

        addRefreshTokenCookie(response, authResult.getRefreshToken());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(authResult.getAccessToken())
                .build());
    }

    private void addRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        String cookie = String.format(
                "refreshToken=%s; HttpOnly; Secure; SameSite=Strict; Path=/auth/refresh; Max-Age=%d",
                refreshToken,
                jwtProperties.getRefreshTokenExpiration());
        response.addHeader("Set-Cookie", cookie);
    }
}
