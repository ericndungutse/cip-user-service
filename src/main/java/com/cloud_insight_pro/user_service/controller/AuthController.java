package com.cloud_insight_pro.user_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud_insight_pro.user_service.dto.APIResponse;
import com.cloud_insight_pro.user_service.dto.APIResponseFactory;
import com.cloud_insight_pro.user_service.dto.SSOLoginRequestDto;
import com.cloud_insight_pro.user_service.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sso-login")
    public ResponseEntity<APIResponse> ssoLogin(@RequestBody @Valid SSOLoginRequestDto requestBody) {
        authService.isSsoDomainSupported(requestBody.getEmail());

        String registrationId = authService.getRegistrationId(requestBody.getEmail());

        String authorizationEndpoint = authService.getAuthorizationEndpoint(registrationId);

        APIResponse response = APIResponseFactory.success("SSO login initiated",
                Map.of("authorizationEndpoint", authorizationEndpoint));

        return ResponseEntity.ok(response);
    }

}
