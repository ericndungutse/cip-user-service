package com.cloud_insight_pro.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cloud_insight_pro.user_service.exceptions.DomainNameNotSupportedException;
import com.cloud_insight_pro.user_service.repository.SsoSupportedDomainRep;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Value("${BASE_URL}")
    private String baseUrl;
    private final SsoSupportedDomainRep ssoSupportedDomainRep;

    public boolean isSsoDomainSupported(String email) {
        String domainName = email.substring(email.indexOf('@') + 1).toLowerCase();
        logger.info("Checking if SSO domain is supported for domain: {}", domainName);
        if (!ssoSupportedDomainRep.existsByDomainName(domainName)) {
            logger.warn("Domain not supported: {}", domainName);
            throw new DomainNameNotSupportedException();
        }
        logger.info("Domain is supported: {}", domainName);
        return true;
    }

    public String getRegistrationId(String email) {
        String domainName = email.substring(email.indexOf('@') + 1).toLowerCase();
        logger.info("Fetching registrationId for domain: {}", domainName);
        String registrationId = ssoSupportedDomainRep.findRegistrationIdByDomainName(domainName)
                .orElseThrow(() -> {
                    logger.warn("RegistrationId not found for domain: {}", domainName);
                    return new DomainNameNotSupportedException();
                });
        logger.info("Found registrationId: {} for domain: {}", registrationId, domainName);
        return registrationId;
    }

    public String getAuthorizationEndpoint(String registrationId) {
        String endpoint = String.format("%s/oauth2/authorization/%s", baseUrl, registrationId);
        logger.info("Generated authorization endpoint: {}", endpoint);
        return endpoint;
    }

}
