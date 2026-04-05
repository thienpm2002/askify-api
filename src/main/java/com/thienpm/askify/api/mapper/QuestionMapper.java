package com.thienpm.askify.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thienpm.askify.api.dto.response.AuthorResponse;
import com.thienpm.askify.api.dto.response.QuestionResponse;
import com.thienpm.askify.api.entity.Question;
import com.thienpm.askify.api.entity.Tag;
import com.thienpm.askify.api.entity.User;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "author", source = "user")
    @Mapping(target = "tags", source = "tags")
    QuestionResponse toResponse(Question question);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "avatarUrl", source = "avatarUrl")
    AuthorResponse toAuthorResponse(User user);

    default String tagToString(Tag tag) {
        return tag.getName();
    }
}
