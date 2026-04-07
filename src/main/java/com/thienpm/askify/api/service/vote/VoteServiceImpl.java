package com.thienpm.askify.api.service.vote;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thienpm.askify.api.dto.request.VoteRequest;
import com.thienpm.askify.api.entity.Vote;
import com.thienpm.askify.api.enums.TargetVoteType;
import com.thienpm.askify.api.repository.AnswerRepository;
import com.thienpm.askify.api.repository.QuestionRepository;
import com.thienpm.askify.api.repository.VoteRepository;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {
    private final VoteRepository voteRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public void vote(VoteRequest request, CustomUserDetails userDetails) {

        Vote existVote = voteRepository.findByUserIdAndTargetIdAndTargetType(
                userDetails.getUser().getId(),
                request.getTargetId(),
                request.getTargetType()).orElse(null);

        int delta = 0;

        if (existVote == null) {
            Vote newVote = Vote.builder()
                    .user(userDetails.getUser())
                    .targetId(request.getTargetId())
                    .targetType(request.getTargetType())
                    .voteType(request.getVoteType())
                    .build();
            voteRepository.save(newVote);
            delta = request.getVoteType().getValue();
        } else {
            if (existVote.getVoteType() == request.getVoteType()) {
                // User is trying to vote the same way again, so we remove the vote
                voteRepository.deleteById(existVote.getId());
                delta = -request.getVoteType().getValue();
            } else {
                // User is changing their vote
                existVote.setVoteType(request.getVoteType());
                voteRepository.save(existVote);
                delta = 2 * request.getVoteType().getValue(); // Change from -1 to 1 or from 1 to -1
            }
        }

        updateVoteCount(request.getTargetId(), request.getTargetType(), delta);
    }

    private void updateVoteCount(Integer targetId, TargetVoteType targetType, int delta) {

        if (targetType == TargetVoteType.QUESTION) {
            questionRepository.incrementVoteCount(targetId, delta);
        } else {
            answerRepository.incrementVoteCount(targetId, delta);
        }
    }
}
