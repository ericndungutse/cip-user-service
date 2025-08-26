package com.cloud_insight_pro.user_service.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.cloud_insight_pro.user_service.model.CustomUserDetails;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION_MS}")
    private String jwtExpirationMS;

    // Generate Jwt token
    public String generateJwtTokenFromUserId(CustomUserDetails userDetails) {
        String userRole = userDetails.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + Integer.parseInt(jwtExpirationMS));
        return Jwts.builder().subject(String.valueOf(userDetails
                .getUserId())).issuedAt(new Date())
                .issuedAt(now)
                .claim("email", userDetails.getEmail())
                .claim("fullName", userDetails.getUser().getFullName())
                .claim("role", userRole)
                .expiration(expiryDate)
                .signWith(key()).compact();
    }

    // Key encryption
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
