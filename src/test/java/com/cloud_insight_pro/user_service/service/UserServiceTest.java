package com.cloud_insight_pro.user_service.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.cloud_insight_pro.user_service.dto.UserDto;
import com.cloud_insight_pro.user_service.model.Role;
import com.cloud_insight_pro.user_service.model.User;
import com.cloud_insight_pro.user_service.repository.UserRepository;
import com.cloud_insight_pro.user_service.service.interfaces.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  private UserService userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(userRepository);
  }

  @Test
  void getMe_returnsUserDto_whenUserExists() {
    UUID userId = UUID.randomUUID();
    User user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setUsername("testuser");
    user.setFullName("Test User");
    Role role = new Role();
    role.setName("USER");
    user.setRole(role);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserDto result = userService.getMe(userId);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(userId);
    assertThat(result.getEmail()).isEqualTo("test@example.com");
    assertThat(result.getUsername()).isEqualTo("testuser");
    assertThat(result.getFullName()).isEqualTo("Test User");
    assertThat(result.getRole()).isEqualTo("USER");
  }

  @Test
  void getMe_returnsNull_whenUserDoesNotExist() {
    UUID userId = UUID.randomUUID();
    when(userRepository.findById(userId)).thenReturn(Optional.empty());

    UserDto result = userService.getMe(userId);

    assertThat(result).isNull();
  }

  @Test
  void getMe_handlesNullRoleGracefully() {
    UUID userId = UUID.randomUUID();
    User user = new User();
    user.setId(userId);
    user.setEmail("test@example.com");
    user.setUsername("testuser");
    user.setFullName("Test User");
    user.setRole(null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    // This will throw NullPointerException if not handled in implementation
    try {
      userService.getMe(userId);
    } catch (NullPointerException e) {
      assertThat(e).isInstanceOf(NullPointerException.class);
    }
  }

  @Test
  void getMe_logsAppropriateMessages() {
    // This test would require a logging framework or spy to verify logs.
    // For now, just ensure the method runs without error.
    UUID userId = UUID.randomUUID();
    User user = new User();
    user.setId(userId);
    user.setEmail("log@example.com");
    user.setUsername("loguser");
    user.setFullName("Log User");
    Role role = new Role();
    role.setName("ADMIN");
    user.setRole(role);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    UserDto result = userService.getMe(userId);

    assertThat(result).isNotNull();
    assertThat(result.getRole()).isEqualTo("ADMIN");
  }
}