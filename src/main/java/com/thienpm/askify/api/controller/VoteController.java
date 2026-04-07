package com.thienpm.askify.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.dto.request.VoteRequest;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.vote.VoteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/votes")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    @PostMapping()
    public ResponseEntity<?> vote(@RequestBody @Valid VoteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        voteService.vote(request, userDetails);
        return ResponseEntity.ok().build();
    }

}
