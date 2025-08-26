package com.cloud_insight_pro.user_service.service.interfaces;

import java.util.UUID;

import com.cloud_insight_pro.user_service.dto.UserDto;

public interface UserService {
  public UserDto getMe(UUID userId);
}
