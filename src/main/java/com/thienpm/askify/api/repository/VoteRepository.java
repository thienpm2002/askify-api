package com.thienpm.askify.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thienpm.askify.api.entity.Vote;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Vote findByUserIdAndTargetIdAndTargetType(Integer userId, Integer targetId, String targetType);

}
