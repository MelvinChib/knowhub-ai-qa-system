package com.knowhub.exception;

/**
 * Custom exception for document processing errors.
 * 
 * <p>This exception is thrown when document upload, text extraction,
 * or processing operations fail. It provides specific error messages
 * to help identify and resolve document-related issues.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public class DocumentProcessingException extends RuntimeException {
    
    /**
     * Constructs a new DocumentProcessingException with the specified message.
     * 
     * @param message the detail message
     */
    public DocumentProcessingException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DocumentProcessingException with the specified message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public DocumentProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}