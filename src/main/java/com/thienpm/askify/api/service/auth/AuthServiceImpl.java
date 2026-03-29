package com.thienpm.askify.api.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thienpm.askify.api.dto.request.RegisterRequestDTO;
import com.thienpm.askify.api.dto.response.AuthResult;
import com.thienpm.askify.api.entity.User;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.enums.Role;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.repository.UserRepository;
import com.thienpm.askify.api.security.jwt.JwtService;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResult register(RegisterRequestDTO registerRequest) {
        String email = registerRequest.getEmail();

        if (userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User user = User.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userName(registerRequest.getUserName())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        UserDetails userDetails = new CustomUserDetails(user);

        return AuthResult.builder()
                .accessToken(jwtService.generateAccessToken(
                        userDetails))
                .refreshToken(jwtService.generateAccessToken(
                        userDetails))
                .build();
    }

}
