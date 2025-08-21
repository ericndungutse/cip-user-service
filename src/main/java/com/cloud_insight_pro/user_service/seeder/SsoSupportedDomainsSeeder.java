package com.cloud_insight_pro.user_service.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cloud_insight_pro.user_service.model.SsoSupportedDomain;
import com.cloud_insight_pro.user_service.repository.SsoSupportedDomainRep;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SsoSupportedDomainsSeeder implements CommandLineRunner {
    private final SsoSupportedDomainRep ssoSupportedDomainsRep;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting SSO Supported Domains seeding process.");

        String domain = "amalitech.com";
        String registrationId = "okta";

        log.debug("Checking if domain '{}' exists in the database.", domain);

        if (!ssoSupportedDomainsRep.existsByDomainName(domain)) {
            log.warn("Domain '{}' not found in database. Seeding with registrationId '{}'.", domain, registrationId);
            try {
                SsoSupportedDomain ssoDomain = new SsoSupportedDomain();
                ssoDomain.setDomainName(domain);
                ssoDomain.setRegistrationId(registrationId);
                ssoSupportedDomainsRep.save(ssoDomain);
                log.info("Successfully seeded domain '{}' with registrationId '{}'.", domain, registrationId);
            } catch (Exception e) {
                log.error("Error occurred while seeding domain '{}': {}", domain, e.getMessage(), e);
            }
        } else {
            log.debug("Domain '{}' already exists in database. Skipping seeding.", domain);
        }

        log.info("SSO Supported Domains seeding process completed.");
    }
}
