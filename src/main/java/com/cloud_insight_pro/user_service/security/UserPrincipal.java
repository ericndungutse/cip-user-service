package com.cloud_insight_pro.user_service.security;

import java.security.Principal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPrincipal implements Principal {
  private String id;
  private String email;
  private String fullName;
  private String role;

  @Override
  public String getName() {
    return fullName;
  }
}
