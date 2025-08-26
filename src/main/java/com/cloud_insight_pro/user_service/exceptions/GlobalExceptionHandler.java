package com.cloud_insight_pro.user_service.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cloud_insight_pro.user_service.dto.APIResponse;
import com.cloud_insight_pro.user_service.dto.APIResponseFactory;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation exception occurred: {}", ex.getMessage(), ex);

        List<Map<String, String>> errors = createValidationErrorList(ex);

        APIResponse response = APIResponseFactory.error("Validation failed", errors);

        log.info("Returning {} validation errors", errors.size());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DomainNameNotSupportedException.class)
    public ResponseEntity<Object> handleDomainNameNotSupportedException(DomainNameNotSupportedException ex) {
        log.error("Domain name not supported: {}", ex.getMessage(), ex);

        log.info("Returning error response for unsupported domain name");

        APIResponse errorResponse = APIResponseFactory.error(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private List<Map<String, String>> createValidationErrorList(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    Map<String, String> errorMap = new HashMap<>();
                    errorMap.put("field", error.getField());
                    errorMap.put("message", error.getDefaultMessage());
                    errors.add(errorMap);
                });
        return errors;
    }

}
