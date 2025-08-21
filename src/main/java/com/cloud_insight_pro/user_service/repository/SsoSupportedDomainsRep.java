package com.cloud_insight_pro.user_service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloud_insight_pro.user_service.model.SsoSupportedDomains;

public interface SsoSupportedDomainsRep extends JpaRepository<SsoSupportedDomains, UUID> {

}
