package com.example.demo.controller;

import com.example.demo.exception.BookNotFoundException;
import com.example.demo.exception.IsbnAlreadyExistsException;
import com.example.demo.models.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ApiError> handleBookNotFoundException(BookNotFoundException ex,
                                                                HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ApiError apiError = this.errorResponse(
                httpStatus,
                ex.getMessage(),
                httpRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(IsbnAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleIsbnAlreadyExistsException(IsbnAlreadyExistsException ex,
                                                                     HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ApiError apiError =  this.errorResponse(
                httpStatus,
                ex.getMessage(),
                httpRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                               HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        Map<String, String> errorDetails = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errorDetails.put(fieldName, errorMessage);
        });

        ApiError apiError = this.errorResponse(
                httpStatus,
                "Validation error for input fields",
                errorDetails,
                httpRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                                          HttpServletRequest httpRequest) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ApiError apiError = this.errorResponse(
                httpStatus,
                ex.getMostSpecificCause().getMessage(),
                httpRequest.getRequestURI()
        );

        return ResponseEntity.status(httpStatus).body(apiError);
    }

    private ApiError errorResponse(HttpStatus status, String message, Object details, String path) {
        return new ApiError()
                .timestamp(LocalDateTime.now())
                .error(status.getReasonPhrase())
                .message(message)
                .details(details)
                .status(status.value())
                .path(path);
    }

    private ApiError errorResponse(HttpStatus status, String message, String path) {
        return new ApiError()
                .timestamp(LocalDateTime.now())
                .error(status.getReasonPhrase())
                .message(message)
                .details(null)
                .status(status.value())
                .path(path);
    }
}
