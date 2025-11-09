package com.knowhub.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the AI-powered intelligent Q&A system.
 * 
 * <p>This class provides centralized exception handling across all controllers,
 * ensuring consistent error responses and proper HTTP status codes. It handles
 * various types of exceptions including validation errors, file upload issues,
 * and application-specific exceptions.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles document processing exceptions.
     * 
     * @param ex the document processing exception
     * @return ResponseEntity with error details and BAD_REQUEST status
     */
    @ExceptionHandler(DocumentProcessingException.class)
    public ResponseEntity<ErrorResponse> handleDocumentProcessingException(DocumentProcessingException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Handles file upload size exceeded exceptions.
     * 
     * @param ex the max upload size exceeded exception
     * @return ResponseEntity with error details and PAYLOAD_TOO_LARGE status
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.PAYLOAD_TOO_LARGE.value(),
            "File size exceeds maximum allowed size",
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    /**
     * Handles validation exceptions for request body validation.
     * 
     * @param ex the method argument not valid exception
     * @return ResponseEntity with field validation errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * Handles all other unexpected exceptions.
     * 
     * @param ex the generic exception
     * @return ResponseEntity with generic error message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected error occurred",
            LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(error);
    }

    /**
     * Error response record for consistent error formatting.
     * 
     * @param status HTTP status code
     * @param message error message
     * @param timestamp when the error occurred
     */
    public record ErrorResponse(int status, String message, LocalDateTime timestamp) {}
}