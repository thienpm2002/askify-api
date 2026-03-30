package com.thienpm.askify.api.service.user;

import org.springframework.stereotype.Service;

import com.thienpm.askify.api.dto.request.UpdateProfileRequest;
import com.thienpm.askify.api.dto.response.UserProfileResponse;
import com.thienpm.askify.api.entity.User;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.repository.UserRepository;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserProfileResponse getProfile(CustomUserDetails userDetails) {

        return UserProfileResponse.builder()
                .id(userDetails.getUser().getId())
                .userName(userDetails.getUser().getUserName())
                .email(userDetails.getUser().getEmail())
                .avatarUrl(userDetails.getUser().getAvatarUrl())
                .build();
    }

    @Override
    public UserProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest, CustomUserDetails userDetails) {
        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newUserName = updateProfileRequest.getUserName();
        if (!user.getUserName().equals(newUserName)) {
            user.setUserName(newUserName);
            userRepository.save(user);
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }

}
