package com.thienpm.askify.api.service.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thienpm.askify.api.dto.projection.QuestionFlatDTO;
import com.thienpm.askify.api.dto.request.CreateQuestionRequest;
import com.thienpm.askify.api.dto.request.QuestionSearchRequest;
import com.thienpm.askify.api.dto.request.UpdateQuestionRequest;
import com.thienpm.askify.api.dto.response.AuthorResponse;
import com.thienpm.askify.api.dto.response.PageQuestionResponse;
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
                List<Tag> tags = processTags(questionRequest.getTags());
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
                if (!isOwner(question.getUser().getId(), userDetails)) {
                        throw new AppException(ErrorCode.FORBIDDEN);
                }

                // Xử lý tags
                List<Tag> tags = processTags(questionRequest.getTags());

                // update
                question.setTags(tags);
                question.setTitle(questionRequest.getTitle());
                question.setContent(questionRequest.getContent());
                questionRepository.save(question);

                return questionMapper.toResponse(question);
        }

        @Transactional
        @Override
        public void deleteQuestion(Integer questionId, CustomUserDetails userDetails) {
                // Kiem tra question
                Question question = questionRepository.findById(questionId)
                                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
                // Kiem tra quyen
                if (!isOwner(question.getUser().getId(), userDetails) && !isAdmin(userDetails)) {
                        throw new AppException(ErrorCode.FORBIDDEN);
                }

                questionRepository.delete(question);

        }

        private boolean isAdmin(CustomUserDetails userDetails) {
                return userDetails.getAuthorities().stream()
                                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        }

        private boolean isOwner(Integer userId, CustomUserDetails userDetails) {
                return userId.equals(userDetails.getUser().getId());
        }

        private String normalized(String tag) {
                return tag.toLowerCase().trim().replaceAll("\\s+", "-");
        }

        private List<Tag> processTags(List<String> tagNames) {
                return tagNames.stream().map(tag -> {
                        String normalized = normalized(tag);
                        return tagRepository.findByName(
                                        normalized)
                                        .orElseGet(() -> tagRepository.save(Tag.builder().name(normalized).build()));
                }).collect(Collectors.toList());
        }

        @Override
        public PageQuestionResponse<QuestionResponse> searchQuestionByTitle(
                        QuestionSearchRequest questionSearchRequest) {

                // B1. Lấy danh sách question có thong tin user theo title với pagination và
                // sorting
                Sort sort = Sort.by(Sort.Direction.fromString(questionSearchRequest.getSortDir()),
                                questionSearchRequest.getSortBy());
                Pageable pageable = PageRequest.of(questionSearchRequest.getPage(),
                                questionSearchRequest.getSize(),
                                sort);
                Page<QuestionFlatDTO> pageResult = questionRepository.searchQuestionByTitle(
                                questionSearchRequest.getKeyword(),
                                pageable);
                List<Integer> questionIds = pageResult.getContent().stream()
                                .map(QuestionFlatDTO::getId)
                                .collect(Collectors.toList());

                // B2. Batch tags theo questionId
                Map<Integer, List<String>> tagMap = new HashMap<>();
                // Nếu không có question nào thì không cần truy vấn tags nữa
                if (!questionIds.isEmpty()) {
                        List<Object[]> tagsResult = questionRepository.findTagsByQuestionIds(questionIds);

                        for (Object[] row : tagsResult) {
                                Integer qId = (Integer) row[0];
                                String tag = (String) row[1];

                                tagMap.computeIfAbsent(qId, k -> new ArrayList<>())
                                                .add(tag);
                        }
                }
                // B3. Map sang QuestionResponse
                List<QuestionResponse> content = pageResult.getContent().stream()
                                .map(q -> QuestionResponse.builder()
                                                .id(q.getId())
                                                .title(q.getTitle())
                                                .content(q.getContent())
                                                .voteCount(q.getVoteCount())
                                                .createdAt(q.getCreatedAt())
                                                .author(AuthorResponse.builder()
                                                                .id(q.getUserId())
                                                                .userName(q.getUserName())
                                                                .avatarUrl(q.getAvatarUrl())
                                                                .build())
                                                .tags(tagMap.getOrDefault(q.getId(), List.of()))
                                                .build())
                                .toList();
                // B4.Trả về kết quả
                return PageQuestionResponse.<QuestionResponse>builder()
                                .content(content)
                                .page(pageResult.getNumber())
                                .size(pageResult.getSize())
                                .totalElements(pageResult.getTotalElements())
                                .totalPages(pageResult.getTotalPages())
                                .build();
        }
}
