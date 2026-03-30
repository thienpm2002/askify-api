package com.thienpm.askify.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thienpm.askify.api.dto.request.UpdateProfileRequest;
import com.thienpm.askify.api.dto.response.UserProfileResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getProfile(userDetails));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestBody @Valid UpdateProfileRequest updateProfileRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.updateProfile(updateProfileRequest, userDetails));
    }

    @PatchMapping("/me/avatar")
    public ResponseEntity<UserProfileResponse> updateAvatar(
            @RequestParam("avatar") MultipartFile avatarFile,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.updateAvatar(avatarFile, userDetails));
    }
}
