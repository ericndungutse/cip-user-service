package com.cloud_insight_pro.user_service.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import com.cloud_insight_pro.user_service.dto.SuccessResponse;
import com.cloud_insight_pro.user_service.dto.UserDto;
import com.cloud_insight_pro.user_service.model.CustomUserDetails;
import com.cloud_insight_pro.user_service.model.User;
import com.cloud_insight_pro.user_service.repository.UserRepository;
import com.cloud_insight_pro.user_service.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
        private final UserRepository userRepository;
        private final ObjectMapper objectMapper;
        private final JwtUtil jwtUtil;
        @Value("${COOKIE_MAX_AGE}")
        private String cookieMaxAge;

        @Bean
        public SecurityFilterChain securityFilterChain(
                        HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .formLogin(AbstractHttpConfigurer::disable)
                                .httpBasic(AbstractHttpConfigurer::disable)
                                .cors(AbstractHttpConfigurer::disable)
                                .logout(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/sso-login").permitAll()
                                                .requestMatchers("/health", "/error").permitAll()
                                                .anyRequest().authenticated())
                                .oauth2Login(oauth2 -> oauth2.successHandler(this::oauth2SuccessHandler));
                return http.build();
        }

        private void oauth2SuccessHandler(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                String email = oidcUser.getEmail();
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        // Create CustomUserDetails for JWT generation
                        CustomUserDetails userDetails = new CustomUserDetails(user);
                        // Generate JWT token
                        String token = jwtUtil.generateJwtTokenFromUserId(userDetails);
                        // Add token cookie
                        Cookie cookie = new Cookie("token", token);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(true);
                        cookie.setPath("/");
                        cookie.setMaxAge(Integer.parseInt(cookieMaxAge));
                        response.addCookie(cookie);
                        UserDto userDto = UserDto.fromUser(user);
                        // Create SuccessResponse
                        SuccessResponse loginResponse = SuccessResponse.builder()
                                        .status("success")
                                        .message("Successfully signed in")
                                        .data(Collections.singletonMap("user", userDto))
                                        .build();
                        // Send response
                        response.setContentType("application/json");
                        response.getWriter()
                                        .write(objectMapper.writeValueAsString(loginResponse));
                } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("User not found");
                }
        }

}
