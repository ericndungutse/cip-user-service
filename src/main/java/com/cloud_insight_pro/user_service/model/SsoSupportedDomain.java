package com.cloud_insight_pro.user_service.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sso_supported_domains")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SsoSupportedDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, name = "domain_name")
    private String domainName;

    @Column(nullable = false, name = "registration_id")
    private String registrationId; // Unique identifier for the registration (google, okta, etc.)
}
