package com.thienpm.askify.api.service.user;

import com.thienpm.askify.api.dto.response.UserProfile;
import com.thienpm.askify.api.security.user.CustomUserDetails;

public interface UserService {

    UserProfile getProfile(CustomUserDetails userDetails);
}
