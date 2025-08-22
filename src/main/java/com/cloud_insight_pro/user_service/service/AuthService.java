package com.cloud_insight_pro.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cloud_insight_pro.user_service.exceptions.DomainNameNotSupportedException;
import com.cloud_insight_pro.user_service.repository.SsoSupportedDomainRep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${BASE_URL}")
    private String baseUrl;
    private final SsoSupportedDomainRep ssoSupportedDomainRep;

    public boolean isSsoDomainSupported(String email) {
        String domainName = email.substring(email.indexOf('@') + 1).toLowerCase();
        if (!ssoSupportedDomainRep.existsByDomainName(domainName)) {
            throw new DomainNameNotSupportedException();
        }
        return true;
    }

    public String getRegistrationId(String email) {
        String domainName = email.substring(email.indexOf('@') + 1).toLowerCase();
        String registrationId = ssoSupportedDomainRep.findRegistrationIdByDomainName(domainName)
                .orElseThrow(() -> new DomainNameNotSupportedException());
        return registrationId;
    }

    public String getAuthorizationEndpoint(String registrationId) {
        return String.format("%s/oauth2/authorization/%s", baseUrl, registrationId);
    }

}
