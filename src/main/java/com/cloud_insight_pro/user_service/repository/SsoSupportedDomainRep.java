package com.cloud_insight_pro.user_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloud_insight_pro.user_service.model.SsoSupportedDomain;

public interface SsoSupportedDomainRep extends JpaRepository<SsoSupportedDomain, UUID> {
    /**
     * Check if a domain exists in the database.
     *
     * @param domain the domain to check
     * @return true if the domain exists, false otherwise
     */
    boolean existsByDomainName(String domainName);

}
