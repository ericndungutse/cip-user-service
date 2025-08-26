package com.cloud_insight_pro.user_service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.cloud_insight_pro.user_service.model.CustomUserDetails;
import com.cloud_insight_pro.user_service.model.Role;
import com.cloud_insight_pro.user_service.model.RoleEnum;
import com.cloud_insight_pro.user_service.model.User;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    private JwtUtil jwtUtil;
    private CustomUserDetails mockUserDetails;
    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        // Set required properties using ReflectionTestUtils
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret", "testSecretKeyForJwtTokenGeneration12345678901234567890");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMS", "3600000"); // 1 hour

        // Create mock objects
        mockRole = new Role();
        mockRole.setId(UUID.randomUUID());
        mockRole.setName(RoleEnum.USER.name());

        mockUser = new User();
        mockUser.setId(UUID.randomUUID());
        mockUser.setEmail("test@example.com");
        mockUser.setFullName("Test User");
        mockUser.setRole(mockRole);

        mockUserDetails = new CustomUserDetails(mockUser);
    }

    @Test
    @DisplayName("generateJwtTokenFromUserId creates valid JWT token")
    void generateJwtTokenFromUserId_createsValidJwtToken() {
        // Act
        String token = jwtUtil.generateJwtTokenFromUserId(mockUserDetails);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3); // JWT has 3 parts
    }

    @Test
    @DisplayName("generated JWT token has correct structure")
    void generatedJwtToken_hasCorrectStructure() {
        // Act
        String token = jwtUtil.generateJwtTokenFromUserId(mockUserDetails);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();

        // Verify JWT structure (header.payload.signature)
        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);
        assertThat(parts[0]).isNotEmpty(); // header
        assertThat(parts[1]).isNotEmpty(); // payload
        assertThat(parts[2]).isNotEmpty(); // signature
    }

    @Test
    @DisplayName("generateJwtTokenFromUserId handles user with empty authorities")
    void generateJwtTokenFromUserId_handlesUserWithEmptyAuthorities() {
        // Arrange - create a mock that returns empty authorities
        CustomUserDetails mockUserDetailsEmptyAuth = mock(CustomUserDetails.class);
        when(mockUserDetailsEmptyAuth.getAuthorities()).thenReturn(Collections.emptyList());
        when(mockUserDetailsEmptyAuth.getUserId()).thenReturn(mockUser.getId());
        when(mockUserDetailsEmptyAuth.getEmail()).thenReturn(mockUser.getEmail());
        when(mockUserDetailsEmptyAuth.getUser()).thenReturn(mockUser);

        // Act
        String token = jwtUtil.generateJwtTokenFromUserId(mockUserDetailsEmptyAuth);

        // Assert
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("JWT token generation handles different users")
    void jwtTokenGeneration_handlesDifferentUsers() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());
        anotherUser.setEmail("another@example.com");
        anotherUser.setFullName("Another User");
        anotherUser.setRole(mockRole);

        CustomUserDetails anotherUserDetails = new CustomUserDetails(anotherUser);

        // Act
        String token1 = jwtUtil.generateJwtTokenFromUserId(mockUserDetails);
        String token2 = jwtUtil.generateJwtTokenFromUserId(anotherUserDetails);

        // Assert
        assertThat(token1).isNotEqualTo(token2);
        assertThat(token1.split("\\.")).hasSize(3);
        assertThat(token2.split("\\.")).hasSize(3);
    }
}
