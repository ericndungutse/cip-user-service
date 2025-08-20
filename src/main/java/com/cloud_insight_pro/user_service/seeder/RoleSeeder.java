package com.cloud_insight_pro.user_service.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cloud_insight_pro.user_service.model.Role;
import com.cloud_insight_pro.user_service.repository.RoleRepository;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedRoles();
    }

    private void seedRoles() {
        Role it_support_role = new Role();
        it_support_role.setName("IT_SUPPORT");

        Role cloud_cost_analyst_role = new Role();
        cloud_cost_analyst_role.setName("CLOUD_COST_ANALYST");

        Role financial_manager_role = new Role();
        financial_manager_role.setName("FINANCIAL_MANAGER");

        Role devops_engineer_role = new Role();
        devops_engineer_role.setName("DEVOPS_ENGINEER");

        Role cto_cio_role = new Role();
        cto_cio_role.setName("CTO_CIO");

        // Check if role already exists before saving
        if (roleRepository.findByName("IT_SUPPORT").isEmpty()) {
            roleRepository.save(it_support_role);
        }
        if (roleRepository.findByName("CLOUD_COST_ANALYST").isEmpty()) {
            roleRepository.save(cloud_cost_analyst_role);
        }
        if (roleRepository.findByName("FINANCIAL_MANAGER").isEmpty()) {
            roleRepository.save(financial_manager_role);
        }
        if (roleRepository.findByName("DEVOPS_ENGINEER").isEmpty()) {
            roleRepository.save(devops_engineer_role);
        }
        if (roleRepository.findByName("CTO_CIO").isEmpty()) {
            roleRepository.save(cto_cio_role);
        }

    }
}
