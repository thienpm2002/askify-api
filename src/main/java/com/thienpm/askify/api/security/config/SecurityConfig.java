package com.thienpm.askify.api.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thienpm.askify.api.dto.response.ErrorResponse;
import com.thienpm.askify.api.enums.ErrorCode;
import com.thienpm.askify.api.security.jwt.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                // 1. Tắt CSRF — REST API dùng JWT không cần CSRF
                .csrf(csrf -> csrf.disable())

                // 2. STATELESS — không tạo HTTP session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Quy định quyền truy cập từng endpoint
                .authorizeHttpRequests(auth -> auth
                        // Cho phép tất cả gọi các endpoint auth
                        .requestMatchers("/api/auth/**").permitAll()
                        // Chỉ ADMIN mới vào được /api/admin/**
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Tất cả còn lại phải đăng nhập
                        .anyRequest().authenticated())

                // 4. Custom 401/403 response
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            new ObjectMapper().writeValue(res.getWriter(),
                                    ErrorResponse.of(ErrorCode.UNAUTHORIZED));
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            new ObjectMapper().writeValue(res.getWriter(),
                                    ErrorResponse.of(ErrorCode.FORBIDDEN));
                        }))

                // 5. Cắm JwtFilter vào trước UsernamePasswordAuthFilter
                .addFilterBefore(jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Bean hash password — LUÔN dùng BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean xác thực — cần để dùng trong AuthService
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
