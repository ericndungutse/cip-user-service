package com.cloud_insight_pro.user_service.exceptions;

import java.util.List;
import java.util.Map;

public class ErrorResponseFactory {
    public static ErrorResponse createErrorResponse(String status, String message, List<Map<String, String>> errors) {
        return new ErrorResponse(status, message, errors);
    }
}
