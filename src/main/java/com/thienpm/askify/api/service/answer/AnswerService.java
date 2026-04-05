package com.thienpm.askify.api.service.answer;

import com.thienpm.askify.api.dto.request.CreateAnswerRequest;
import com.thienpm.askify.api.dto.response.AnswerResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;

public interface AnswerService {
    AnswerResponse createAnswer(CreateAnswerRequest request, CustomUserDetails userDetails);
}
