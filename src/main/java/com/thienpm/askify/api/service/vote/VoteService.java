package com.thienpm.askify.api.service.vote;

import com.thienpm.askify.api.dto.request.VoteRequest;
import com.thienpm.askify.api.security.user.CustomUserDetails;

public interface VoteService {
    void vote(VoteRequest request, CustomUserDetails userDetails);
}
