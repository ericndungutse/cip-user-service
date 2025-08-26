package com.cloud_insight_pro.user_service.dto;

import java.util.UUID;

import com.cloud_insight_pro.user_service.model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private UUID id;
  private String username;
  private String fullName;
  private String email;
  private String role;
  private String profilePicture;

  public static UserDto fromUser(User user) {
    if (user == null)
      return null;
    return new UserDto(
        user.getId(),
        user.getUsername(),
        user.getFullName(),
        user.getEmail(),
        user.getRole().getName(),
        user.getProfilePicture());
  }
}