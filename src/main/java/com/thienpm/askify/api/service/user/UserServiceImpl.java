package com.thienpm.askify.api.service.user;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileStorageService fileStorageService;

    @Override
    @Cacheable(value = "userProfile", key = "#userDetails.getUser().getId()")
    public UserProfileResponse getProfile(CustomUserDetails userDetails) {

        return UserProfileResponse.builder()
                .id(userDetails.getUser().getId())
                .userName(userDetails.getUser().getUserName())
                .email(userDetails.getUser().getEmail())
                .avatarUrl(userDetails.getUser().getAvatarUrl())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "userProfile", key = "#userDetails.getUser().getId()")
    public UserProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest, CustomUserDetails userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newUserName = updateProfileRequest.getUserName();
        if (!user.getUserName().equals(newUserName)) {
            user.setUserName(newUserName);
        }

        String newEmail = updateProfileRequest.getEmail();
        if (!user.getEmail().equals(newEmail)) {
            user.setEmail(newEmail);
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }

    @Override
    @Transactional
    @CacheEvict(value = "userProfile", key = "#userDetails.getUser().getId()")
    public UserProfileResponse updateAvatar(MultipartFile avatarFile, CustomUserDetails userDetails) {

        Integer id = userDetails.getUser().getId();
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String newAvatarUrl = fileStorageService.saveAvatar(avatarFile, id);

        user.setAvatarUrl(newAvatarUrl);

        return UserProfileResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }

}
