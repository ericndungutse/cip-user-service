package com.cloud_insight_pro.user_service.exceptions;

import java.util.List;
import java.util.Map;

public record ErrorResponse(String status, String message, List<Map<String, String>> errors) {
}