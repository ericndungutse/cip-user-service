package com.cloud_insight_pro.user_service.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cloud_insight_pro.user_service.dto.UserDto;
import com.cloud_insight_pro.user_service.model.User;
import com.cloud_insight_pro.user_service.repository.UserRepository;
import com.cloud_insight_pro.user_service.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public UserDto getMe(UUID userId) {
    log.info("Fetching user with ID: {}", userId);
    Optional<User> user = userRepository.findById(userId);

    if (user.isEmpty()) {
      log.warn("User with ID {} not found", userId);
      return null;
    }

    UserDto userDto = UserDto.builder()
        .id(user.get().getId())
        .email(user.get().getEmail())
        .username(user.get().getUsername())
        .fullName(user.get().getFullName())
        .role(user.get().getRole().getName())
        .build();

    log.info("Successfully fetched user: {}", userDto.getUsername());
    return userDto;
  }
}
