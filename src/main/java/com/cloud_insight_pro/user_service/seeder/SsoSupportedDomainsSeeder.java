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
        // Seed initial data (Domain: amalitech.com => Registration ID: googl or
        // amalitech, or okta)

        // Seed database with amalitech.com to okta if does not exists
        if (!ssoSupportedDomainsRep.existsByDomainName("amalitech.com")) {
            SsoSupportedDomain ssoDomain = new SsoSupportedDomain();
            ssoDomain.setDomainName("amalitech.com");
            ssoDomain.setRegistrationId("okta");
            ssoSupportedDomainsRep.save(ssoDomain);
        }
    }
}
