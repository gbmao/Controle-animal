package com.projeto.controleanimal.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> body = new LinkedHashMap<>();

        HttpStatus httpStatus = HttpStatus.valueOf(ex.getStatusCode().value());

        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", httpStatus.value());
        body.put("error", httpStatus.getReasonPhrase());
        body.put("message", ex.getReason());
        body.put("path", request.getRequestURI());

        return new ResponseEntity<>(body, httpStatus);
    }
}

