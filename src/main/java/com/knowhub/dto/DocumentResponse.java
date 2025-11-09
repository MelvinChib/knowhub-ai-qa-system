package com.knowhub.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for document information responses.
 * 
 * <p>This record represents document metadata returned to clients
 * after successful upload or when listing documents. It excludes
 * sensitive information like file paths and extracted text content.</p>
 * 
 * @param id unique identifier of the document
 * @param filename system-generated filename
 * @param originalFilename original filename as uploaded by user
 * @param contentType MIME type of the document
 * @param fileSize size of the document in bytes
 * @param uploadedAt timestamp when the document was uploaded
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public record DocumentResponse(
    Long id,
    String filename,
    String originalFilename,
    String contentType,
    Long fileSize,
    LocalDateTime uploadedAt
) {}