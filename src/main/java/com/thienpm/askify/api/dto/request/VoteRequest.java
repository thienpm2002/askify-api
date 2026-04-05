package com.thienpm.askify.api.dto.request;

import com.thienpm.askify.api.enums.TargetVoteType;
import com.thienpm.askify.api.enums.VoteType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VoteRequest {

    @NotNull(message = "Target ID is required")
    @Positive(message = "Target ID must be greater than 0")
    private Integer targetId;

    @NotNull(message = "Target type is required")
    private TargetVoteType targetType;

    @NotNull(message = "Vote type is required")
    private VoteType voteType;
}