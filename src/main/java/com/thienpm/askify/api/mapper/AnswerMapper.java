package com.thienpm.askify.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thienpm.askify.api.dto.response.AnswerResponse;
import com.thienpm.askify.api.dto.response.AuthorResponse;
import com.thienpm.askify.api.entity.Answer;
import com.thienpm.askify.api.entity.Question;
import com.thienpm.askify.api.entity.User;

@Mapper(componentModel = "spring")
public interface AnswerMapper {

    @Mapping(target = "author", source = "user")
    @Mapping(target = "questionId", source = "question")
    AnswerResponse toResponse(Answer answer);

    AuthorResponse toAuthorResponse(User user);

    default Integer mapQuestionToId(Question question) {
        return question != null ? question.getId() : null;
    }
}
