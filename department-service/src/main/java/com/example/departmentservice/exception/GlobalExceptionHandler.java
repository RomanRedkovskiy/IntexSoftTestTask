package com.example.departmentservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        return generateResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        return generateResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(DataIntegrityViolationException e) {
        return generateResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(MethodArgumentNotValidException e) {
        // To return passed message
        String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return generateResponseEntity(HttpStatus.BAD_REQUEST, errorMessage);
    }

    private ResponseEntity<ExceptionResponse> generateResponseEntity(HttpStatus httpStatus, String message) {
        ExceptionResponse response = new ExceptionResponse(LocalDateTime.now(), message);
        return ResponseEntity.status(httpStatus).body(response);
    }
}