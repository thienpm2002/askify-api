package com.thienpm.askify.api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.dto.request.CreateAnswerRequest;
import com.thienpm.askify.api.dto.request.UpdateAnswerRequest;
import com.thienpm.askify.api.dto.response.AnswerResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.answer.AnswerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping()
    public ResponseEntity<AnswerResponse> postMethodName(@RequestBody @Valid CreateAnswerRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.created(null).body(answerService.createAnswer(request, userDetails));
    }

    @PatchMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> editAnswer(@PathVariable Integer answerId,
            @RequestBody @Valid UpdateAnswerRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(answerService.editAnswer(answerId, request, userDetails));
    }

}
