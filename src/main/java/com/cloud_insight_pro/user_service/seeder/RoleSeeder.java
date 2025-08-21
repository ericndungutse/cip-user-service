package com.cloud_insight_pro.user_service.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cloud_insight_pro.user_service.model.Role;
import com.cloud_insight_pro.user_service.model.RoleEnum;
import com.cloud_insight_pro.user_service.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting RoleSeeder...");
        seedRoles();
        log.info("RoleSeeder finished.");
    }

    private void seedRoles() {
        log.debug("Seeding roles...");
        for (RoleEnum role : RoleEnum.values()) {
            log.trace("Checking if role '{}' exists...", role.name());
            if (!roleRepository.existsByName(role.name())) {
                log.info("Role '{}' does not exist. Creating...", role.name());
                Role newRole = new Role();
                newRole.setName(role.name());
                roleRepository.save(newRole);
                log.debug("Role '{}' created and saved.", role.name());
            } else {
                log.warn("Role '{}' already exists. Skipping...", role.name());
            }
        }
        log.debug("Role seeding completed.");
    }
}
