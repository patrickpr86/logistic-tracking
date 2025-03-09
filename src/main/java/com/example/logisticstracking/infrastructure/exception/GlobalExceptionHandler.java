package com.example.logisticstracking.infrastructure.exception;

import com.example.logisticstracking.domain.exception.InvalidStatusTransitionException;
import com.example.logisticstracking.domain.exception.PackageNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatusTransition(
            InvalidStatusTransitionException ex, HttpServletRequest request) {

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.CONFLICT.value(),
                "error", "Invalid status transition",
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PackageNotFoundException.class)
    public ResponseEntity<Map<String, Object>> packageNotFoundException(
            PackageNotFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Package not found!",
                "message", ex.getMessage(),
                "path", request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

}
