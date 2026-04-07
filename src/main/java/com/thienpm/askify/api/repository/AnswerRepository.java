package com.thienpm.askify.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.thienpm.askify.api.entity.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    @Modifying
    @Query("UPDATE Answer a SET a.voteCount = a.voteCount + :delta WHERE a.id = :id")
    void incrementVoteCount(@Param("id") Integer id, @Param("delta") int delta);
}
