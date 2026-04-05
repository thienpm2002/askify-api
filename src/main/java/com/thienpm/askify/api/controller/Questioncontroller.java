package com.thienpm.askify.api.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.request.QuestionSearchRequest;
import com.thienpm.askify.api.dto.request.UpdateQuestionRequest;
import com.thienpm.askify.api.dto.response.PageQuestionResponse;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.service.question.QuestionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable Integer questionId) {
        return ResponseEntity.ok(questionService.getQuestionById(questionId));
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionResponse> updateQuestionById(@PathVariable Integer questionId,
            @RequestBody @Valid UpdateQuestionRequest questionRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(questionService.updateQuestion(questionId, questionRequest, userDetails));
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable Integer questionId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        questionService.deleteQuestion(questionId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageQuestionResponse<QuestionResponse>> getAllQuestionByTitle(
            @ModelAttribute @Valid QuestionSearchRequest questionSearchRequest) {
        return ResponseEntity.ok(questionService.searchQuestionByTitle(questionSearchRequest));
    }

}
