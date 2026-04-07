package com.thienpm.askify.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thienpm.askify.api.entity.Vote;
import com.thienpm.askify.api.enums.TargetVoteType;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByUserIdAndTargetIdAndTargetType(Integer userId, Integer targetId, TargetVoteType targetType);

}
