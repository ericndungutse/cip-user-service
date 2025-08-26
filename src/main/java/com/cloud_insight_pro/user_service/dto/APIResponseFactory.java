package com.cloud_insight_pro.user_service.dto;

import java.util.List;
import java.util.Map;

public class APIResponseFactory {

  // Success response without data
  public static APIResponse success(String message) {
    return APIResponse.builder()
        .status("success")
        .message(message)
        .build();
  }

  // Success response with data
  public static APIResponse success(String message, Object data) {
    return APIResponse.builder()
        .status("success")
        .message(message)
        .data(data)
        .build();
  }

  // Error response without errors
  public static APIResponse error(String message) {
    return APIResponse.builder()
        .status("fail")
        .message(message)
        .build();
  }

  // Error Response with errors
  public static APIResponse error(String message, List<Map<String, String>> errors) {
    return APIResponse.builder()
        .status("fail")
        .message(message)
        .errors(errors)
        .build();
  }

}
