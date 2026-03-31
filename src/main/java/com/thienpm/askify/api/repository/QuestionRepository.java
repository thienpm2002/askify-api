package com.thienpm.askify.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thienpm.askify.api.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer> {

}
