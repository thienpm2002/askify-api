package com.thienpm.askify.api.service.user;

import org.springframework.stereotype.Service;

import com.thienpm.askify.api.dto.response.UserProfile;
import com.thienpm.askify.api.repository.UserRepository;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserProfile getProfile(CustomUserDetails userDetails) {

        return UserProfile.builder()
                .id(userDetails.getUser().getId())
                .userName(userDetails.getUser().getUserName())
                .email(userDetails.getUser().getEmail())
                .avatarUrl(userDetails.getUser().getAvatarUrl())
                .build();
    }

}
