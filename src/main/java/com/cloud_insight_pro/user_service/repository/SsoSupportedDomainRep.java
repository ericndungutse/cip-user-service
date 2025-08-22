package com.cloud_insight_pro.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloud_insight_pro.user_service.model.SsoSupportedDomain;

public interface SsoSupportedDomainRep extends JpaRepository<SsoSupportedDomain, UUID> {

    boolean existsByDomainName(String domainName);

    @Query("SELECT s.registrationId FROM SsoSupportedDomain s WHERE s.domainName = :domainName")
    Optional<String> findRegistrationIdByDomainName(@Param("domainName") String domainName);

}
