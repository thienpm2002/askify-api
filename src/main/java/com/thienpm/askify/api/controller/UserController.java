package com.thienpm.askify.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.dto.request.UpdateProfileRequest;
import com.thienpm.askify.api.dto.response.UserProfile;
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
    public ResponseEntity<UserProfile> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.getProfile(userDetails));
    }

    @PatchMapping("/me")
    public ResponseEntity<UserProfile> updateProfile(@RequestBody @Valid UpdateProfileRequest updateProfileRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(userService.updateProfile(updateProfileRequest, userDetails));
    }
}
