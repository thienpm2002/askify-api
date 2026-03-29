package com.thienpm.askify.api.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.thienpm.askify.api.config.JwtProperties;
import com.thienpm.askify.api.security.user.CustomUserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }

    // Tạo token
    private String generateToken(UserDetails userDetails, long expiration) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList());

        String userId = ((CustomUserDetails) userDetails).getId().toString();

        return Jwts.builder()
                .subject(userId) // sub claims: userId
                .claims(extraClaims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration
                        * 1000))
                .signWith(secretKey)
                .compact();
    }

    // Access Token
    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtProperties.getAccessTokenExpiration());
    }

    // Refresh Token
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtProperties.getRefreshTokenExpiration());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = getClaims(token);

        String userId = ((CustomUserDetails) userDetails).getId().toString();

        return claims.getSubject().equals(userId)
                && !claims.getExpiration().before(new Date());
    }

    public Integer extractUserId(String token) {
        return Integer.parseInt(getClaims(token).getSubject());
    }

}
