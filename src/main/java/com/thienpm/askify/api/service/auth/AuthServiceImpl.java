package com.thienpm.askify.api.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thienpm.askify.api.dto.request.LoginRequestDTO;
import com.thienpm.askify.api.dto.request.RegisterRequestDTO;
import com.thienpm.askify.api.dto.response.AuthResult;
import com.thienpm.askify.api.entity.User;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.exception.AppException;
import com.thienpm.askify.api.repository.UserRepository;
import com.thienpm.askify.api.security.jwt.JwtService;
import com.thienpm.askify.api.security.user.CustomUserDetails;
import com.thienpm.askify.api.security.user.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final CustomUserDetailsService userDetailsService;

        @Override
        public AuthResult register(RegisterRequestDTO registerRequest) {
                String email = registerRequest.getEmail();

                if (userRepository.existsByEmail(email)) {
                        throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
                }

                User user = User.builder()
                                .email(email)
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                .userName(registerRequest.getUserName())
                                .build();

                userRepository.save(user);

                UserDetails userDetails = new CustomUserDetails(user);

                return AuthResult.builder()
                                .accessToken(jwtService.generateAccessToken(
                                                userDetails))
                                .refreshToken(jwtService.generateRefreshToken(
                                                userDetails))
                                .build();
        }

        @Override
        public AuthResult login(LoginRequestDTO loginRequest) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                                                loginRequest.getPassword()));
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                return AuthResult.builder()
                                .accessToken(jwtService.generateAccessToken(
                                                userDetails))
                                .refreshToken(jwtService.generateRefreshToken(
                                                userDetails))
                                .build();
        }

        @Override
        public AuthResult refreshToken(String refreshToken) {
                // Extract userId từ refresh token
                Integer userId = jwtService.extractUserId(refreshToken);

                // Load user
                UserDetails userDetails = userDetailsService.loadUserById(userId);

                // Generate cả 2 token mới (refresh token rotation)
                return AuthResult.builder()
                                .accessToken(jwtService.generateAccessToken(userDetails))
                                .refreshToken(jwtService.generateRefreshToken(userDetails))
                                .build();

        }

}
