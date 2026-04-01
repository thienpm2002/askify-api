package com.thienpm.askify.api.service.question;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.request.UpdateQuestionRequest;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.entity.Question;
import com.thienpm.askify.api.entity.Tag;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.mapper.QuestionMapper;
import com.thienpm.askify.api.repository.QuestionRepository;
import com.thienpm.askify.api.repository.TagRepository;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TagRepository tagRepository;
    private final QuestionMapper questionMapper;

    @Override
    public QuestionResponse createQuestion(CreateQuestionRequest questionRequest, CustomUserDetails userDetails) {
        // Xu ly tags
        List<Tag> tags = questionRequest.getTags().stream().map(tag -> {
            String tagName = tag.toLowerCase().trim().replaceAll("\\s+", "-");
            return tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
        }).collect(Collectors.toList());

        // Tao question
        Question question = Question.builder()
                .title(questionRequest.getTitle())
                .content(questionRequest.getContent())
                .user(userDetails.getUser())
                .tags(tags)
                .build();

        questionRepository.save(question);

        // Mapper -> Response
        return questionMapper.toResponse(question);
    }

    @Override
    public QuestionResponse getQuestionById(Integer questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        return questionMapper.toResponse(question);
    }

    @Transactional
    @Override
    public QuestionResponse updateQuestion(Integer questionId, UpdateQuestionRequest questionRequest,
            CustomUserDetails userDetails) {
        // Kiem tra question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        // Kiem tra user
        if (!question.getUser().getId().equals(userDetails.getUser().getId())) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        // Xử lý tags
        List<Tag> tags = questionRequest.getTags().stream().map(tag -> {
            String tagName = tag.toLowerCase().trim().replaceAll("\\s+", "-");
            return tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(Tag.builder().name(tagName).build()));
        }).collect(Collectors.toList());

        // update
        question.setTags(tags);
        question.setTitle(questionRequest.getTitle());
        question.setContent(questionRequest.getContent());
        questionRepository.save(question);

        return questionMapper.toResponse(question);
    }

}
