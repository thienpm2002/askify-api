package com.thienpm.askify.api.service.answer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thienpm.askify.api.dto.request.CreateAnswerRequest;
import com.thienpm.askify.api.dto.request.UpdateAnswerRequest;
import com.thienpm.askify.api.dto.response.AnswerResponse;
import com.thienpm.askify.api.entity.Answer;
import com.thienpm.askify.api.entity.Question;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.mapper.AnswerMapper;
import com.thienpm.askify.api.repository.AnswerRepository;
import com.thienpm.askify.api.repository.QuestionRepository;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final AnswerMapper answerMapper;

    @Override
    public AnswerResponse createAnswer(CreateAnswerRequest request, CustomUserDetails userDetails) {

        // Tim question theo questionId, neu khong tim thay thi throw exception
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        // Tao answer
        Answer answer = Answer.builder()
                .content(request.getContent())
                .user(userDetails.getUser())
                .question(question)
                .build();

        answerRepository.save(answer);

        return answerMapper.toResponse(answer);

    }

    @Transactional
    @Override
    public AnswerResponse editAnswer(Integer answerId, UpdateAnswerRequest request, CustomUserDetails userDetails) {
        // Tim answer theo ud
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
        // Kiem tra xem user co phai la tac gia cua answer hay khong
        if (!answer.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }
        // Edit answer
        if (!answer.getContent().equals(request.getContent())) {
            answer.setContent(request.getContent());
        }

        return answerMapper.toResponse(answer);
    }

}
