package com.thienpm.askify.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.question.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/questions")
@RequiredArgsConstructor
public class Questioncontroller {
    private final QuestionService questionService;

    @PostMapping()
    public ResponseEntity<QuestionResponse> createQuestion(@RequestBody @Valid CreateQuestionRequest questionRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        QuestionResponse response = questionService.createQuestion(questionRequest, userDetails);
        URI location = URI.create("/questions/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

}
