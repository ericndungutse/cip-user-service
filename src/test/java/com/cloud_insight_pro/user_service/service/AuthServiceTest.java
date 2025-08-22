package com.cloud_insight_pro.user_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cloud_insight_pro.user_service.exceptions.DomainNameNotSupportedException;
import com.cloud_insight_pro.user_service.repository.SsoSupportedDomainRep;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private SsoSupportedDomainRep ssoSupportedDomainRep;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(ssoSupportedDomainRep);
    }

    @Test
    void isSsoDomainSupported_returnsTrue_whenDomainExists() {
        when(ssoSupportedDomainRep.existsByDomainName("example.com")).thenReturn(true);

        boolean result = authService.isSsoDomainSupported("user@example.com");

        assertThat(result).isTrue();
    }

    @Test
    void isSsoDomainSupported_throws_whenDomainMissing() {
        when(ssoSupportedDomainRep.existsByDomainName("unknown.com")).thenReturn(false);

        assertThrows(DomainNameNotSupportedException.class, () -> authService.isSsoDomainSupported("user@unknown.com"));
    }

    @Test
    void getRegistrationId_returnsId_whenDomainExists() {
        when(ssoSupportedDomainRep.findRegistrationIdByDomainName("example.com"))
                .thenReturn(java.util.Optional.of("google"));

        String registrationId = authService.getRegistrationId("user@example.com");

        assertThat(registrationId).isEqualTo("google");
    }

    @Test
    void getRegistrationId_throws_whenDomainMissing() {
        when(ssoSupportedDomainRep.findRegistrationIdByDomainName("unknown.com"))
                .thenReturn(java.util.Optional.empty());

        assertThrows(DomainNameNotSupportedException.class, () -> authService.getRegistrationId("user@unknown.com"));
    }

    @Test
    void getAuthorizationEndpoint_buildsCorrectUrl() {
        ReflectionTestUtils.setField(authService, "baseUrl", "https://myapp.example");

        String endpoint = authService.getAuthorizationEndpoint("google");

        assertThat(endpoint).isEqualTo("https://myapp.example/oauth2/authorization/google");
    }
}
