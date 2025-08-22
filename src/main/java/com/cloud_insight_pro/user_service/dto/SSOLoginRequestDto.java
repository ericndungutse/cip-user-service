package com.cloud_insight_pro.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SSOLoginRequestDto {
    @NotEmpty(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;
}