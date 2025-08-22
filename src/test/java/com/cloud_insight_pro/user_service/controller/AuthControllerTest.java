package com.cloud_insight_pro.user_service.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cloud_insight_pro.user_service.exceptions.DomainNameNotSupportedException;
import com.cloud_insight_pro.user_service.exceptions.GlobalExceptionHandler;
import com.cloud_insight_pro.user_service.service.AuthService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("sso-login returns success with authorization endpoint for valid email")
    void ssoLogin_success() throws Exception {
        when(authService.isSsoDomainSupported("user@example.com")).thenReturn(true);
        when(authService.getRegistrationId("user@example.com")).thenReturn("okta");
        when(authService.getAuthorizationEndpoint("okta")).thenReturn("https://app/oauth2/authorization/okta");

        String json = "{\"email\":\"user@example.com\"}";

        mockMvc.perform(post("/api/v1/auth/sso-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("success")))
                .andExpect(jsonPath("$.message", is("SSO login initiated")))
                .andExpect(jsonPath("$.data", is("https://app/oauth2/authorization/okta")));
    }

    @Test
    @DisplayName("sso-login returns 400 when email is missing or blank (validation)")
    void ssoLogin_validationError() throws Exception {
        String json = "{\"email\":\"\"}";

        mockMvc.perform(post("/api/v1/auth/sso-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    @DisplayName("sso-login maps DomainNameNotSupportedException to 400 with fail status")
    void ssoLogin_domainNotSupported() throws Exception {
        when(authService.isSsoDomainSupported("user@unknown.com")).thenThrow(new DomainNameNotSupportedException());

        String json = "{\"email\":\"user@unknown.com\"}";

        mockMvc.perform(post("/api/v1/auth/sso-login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("fail")))
                .andExpect(jsonPath("$.message", is("Domain name is not supported.")))
                .andExpect(jsonPath("$.errors", nullValue()));
    }
}
