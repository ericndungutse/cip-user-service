package com.cloud_insight_pro.user_service.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloud_insight_pro.user_service.dto.APIResponse;
import com.cloud_insight_pro.user_service.dto.APIResponseFactory;
import com.cloud_insight_pro.user_service.dto.UserDto;
import com.cloud_insight_pro.user_service.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<APIResponse> getMe(@RequestHeader("X-User-Id") String userIdFromHeader) {
    // Get user id from header X-User-Id
    UUID userId = UUID.fromString(userIdFromHeader);

    // Load User from database
    UserDto userDto = userService.getMe(userId);

    APIResponse response = APIResponseFactory
        .success("User details fetched successfully", Map.of("user", userDto));

    return ResponseEntity.ok(response);
  }

}
