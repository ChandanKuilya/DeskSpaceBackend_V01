package com.ck.deskspace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catch RuntimeException (The one you just saw in the console)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage()); // Returns "Workspace is already booked..."

        // Return 400 Bad Request instead of 500 or 403
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // You can add more handlers here (e.g., for Validation errors)
}
