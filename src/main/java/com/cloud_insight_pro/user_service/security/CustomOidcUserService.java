package com.cloud_insight_pro.user_service.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.cloud_insight_pro.user_service.model.Role;
import com.cloud_insight_pro.user_service.model.RoleEnum;
import com.cloud_insight_pro.user_service.model.User;
import com.cloud_insight_pro.user_service.repository.RoleRepository;
import com.cloud_insight_pro.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.debug("Starting OIDC user loading process");

        OidcUser oidcUser = super.loadUser(userRequest);
        log.info("Loaded OIDC user: {}", oidcUser.getSubject());

        String nickname = oidcUser.getAttribute("nickname");
        String email = oidcUser.getAttribute("email");
        String fullName = oidcUser.getAttribute("name");
        String picture = oidcUser.getAttribute("picture");

        log.debug("Extracted attributes - nickname: {}, email: {}, fullName: {}, picture: {}", nickname, email,
                fullName, picture);

        Role userRole;
        try {
            userRole = roleRepository.findByName(RoleEnum.USER.name())
                    .orElseThrow(() -> new RuntimeException("User role not found"));
            log.debug("User role found: {}", userRole.getName());
        } catch (RuntimeException e) {
            log.error("Failed to find user role: {}", e.getMessage());
            throw e;
        }

        if (userRepository.findByEmail(email).isEmpty()) {
            log.info("No user found with email {}. Creating new user.", email);
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(nickname);
            newUser.setFullName(fullName);
            newUser.setProfilePicture(picture);
            newUser.setRole(userRole);
            userRepository.save(newUser);
            log.info("New user created and saved: {}", email);
        } else {
            log.warn("User with email {} already exists. Skipping creation.", email);
        }

        log.debug("Returning loaded OIDC user");
        return oidcUser;
    }
}
