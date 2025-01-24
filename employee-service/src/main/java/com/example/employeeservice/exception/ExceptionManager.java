package com.example.employeeservice.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ExceptionManager {

    private static final Logger log = LoggerFactory.getLogger(ExceptionManager.class);

    public static String getResponseMessageFromFeignException(FeignException e) {
        String content = e.contentUTF8();
        HttpStatus httpStatus = getHttpStatus(e.status());
        return parseExceptionMessage(content, httpStatus);
    }

    private static HttpStatus getHttpStatus(int statusCode) {
        try {
            return HttpStatus.valueOf(statusCode);
        } catch (IllegalArgumentException ex) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private static String parseExceptionMessage(String content, HttpStatus httpStatus) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        if (content == null || content.isBlank()) {
            log.error("Service sent no exception reason message or service is unavailable");
            return httpStatus.getReasonPhrase(); // Fallback to HTTP reason if content is blank
        }
        try {
            ExceptionResponse exceptionResponse = objectMapper.readValue(content, ExceptionResponse.class);
            return exceptionResponse.getMessage(); // Return the parsed exception message
        } catch (Exception ex) {
            log.error("Error parsing FeignException content: {}", ex.getMessage(), ex);
            return httpStatus.getReasonPhrase(); // Fallback to HTTP reason if deserialization fails
        }
    }

    static ResponseEntity<ExceptionResponse> generateResponseEntity(HttpStatus httpStatus, String message) {
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), message);
        return ResponseEntity.status(httpStatus).body(response);
    }

}
