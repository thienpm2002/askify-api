package com.thienpm.askify.api.service.question;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.entity.Question;
import com.thienpm.askify.api.entity.Tag;
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
        }).toList();

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

}
