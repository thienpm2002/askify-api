package com.thienpm.askify.api.service.user;

import org.springframework.web.multipart.MultipartFile;

import com.thienpm.askify.api.dto.request.UpdateProfileRequest;
import com.thienpm.askify.api.dto.response.UserProfileResponse;
import com.thienpm.askify.api.security.user.CustomUserDetails;

public interface UserService {

    UserProfileResponse getProfile(CustomUserDetails userDetails);

    UserProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest, CustomUserDetails userDetails);

    UserProfileResponse updateAvatar(MultipartFile avatarFile, CustomUserDetails userDetails);
}