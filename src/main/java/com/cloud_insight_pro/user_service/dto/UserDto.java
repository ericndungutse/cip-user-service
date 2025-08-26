package com.cloud_insight_pro.user_service.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
  private UUID id;
  private String username;
  private String fullName;
  private String email;
  private String role;
  private String profilePicture;
}