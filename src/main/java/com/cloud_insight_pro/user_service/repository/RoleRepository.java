package com.cloud_insight_pro.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cloud_insight_pro.user_service.model.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    /**
     * Find a role by its name.
     *
     * @param name the name of the role
     * @return the Optional role with the specified name, or empty if not found
     */
    Optional<Role> findByName(String name);

    /**
     * Check if a role exists by its name.
     *
     * @param name the name of the role
     * @return true if a role with the specified name exists, false otherwise
     */
    boolean existsByName(String name);

}