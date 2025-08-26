package com.cloud_insight_pro.user_service.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cloud_insight_pro.user_service.dto.UserDto;
import com.cloud_insight_pro.user_service.exceptions.GlobalExceptionHandler;
import com.cloud_insight_pro.user_service.service.interfaces.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("getMe returns user details for valid X-User-Id header")
  void getMe_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto userDto = UserDto.builder()
        .id(userId)
        .fullName("John Doe")
        .email("john.doe@example.com")
        .build();

    // Mock service call
    when(userService.getMe(userId)).thenReturn(userDto);

    mockMvc.perform(get("/api/v1/users/me")
        .header("X-User-Id", userId.toString())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status", is("success")))
        .andExpect(jsonPath("$.message", is("User details fetched successfully")))
        .andExpect(jsonPath("$.data.user.id", is(userId.toString())))
        .andExpect(jsonPath("$.data.user.fullName", is("John Doe")))
        .andExpect(jsonPath("$.data.user.email", is("john.doe@example.com")));
  }
}