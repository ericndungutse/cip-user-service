package com.cloud_insight_pro.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud_insight_pro.user_service.dto.SSOLoginRequestDto;
import com.cloud_insight_pro.user_service.dto.SuccessResponse;
import com.cloud_insight_pro.user_service.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sso-login")
    public ResponseEntity<SuccessResponse> ssoLogin(@RequestBody @Valid SSOLoginRequestDto requestBody) {

        authService.isSsoDomainSupported(requestBody.getEmail());

        String registrationId = authService.getRegistrationId(requestBody.getEmail());

        String authorizationEndpoint = authService.getAuthorizationEndpoint(registrationId);

        SuccessResponse response = new SuccessResponse();
        response.setStatus("success");
        response.setMessage("SSO login initiated");
        response.setData(authorizationEndpoint);

        return ResponseEntity.ok(response);
    }

}
