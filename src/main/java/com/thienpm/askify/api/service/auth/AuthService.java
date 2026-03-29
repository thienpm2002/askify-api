package com.thienpm.askify.api.service.auth;

import com.thienpm.askify.api.dto.request.RegisterRequestDTO;
import com.thienpm.askify.api.dto.response.AuthResult;

public interface AuthService {
    AuthResult register(RegisterRequestDTO registerRequest);
}
