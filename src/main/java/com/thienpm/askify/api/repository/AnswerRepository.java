package com.thienpm.askify.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thienpm.askify.api.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {

}
