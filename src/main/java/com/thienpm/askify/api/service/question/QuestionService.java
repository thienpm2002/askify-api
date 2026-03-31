package com.thienpm.askify.api.service.question;

import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;

public interface QuestionService {
    QuestionResponse createQuestion(CreateQuestionRequest questionRequest, CustomUserDetails userDetails);

    QuestionResponse getQuestionById(Integer questionId);
}
