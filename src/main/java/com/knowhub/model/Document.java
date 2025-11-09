package com.knowhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a document uploaded to the KnowHub system.
 * 
 * <p>This entity stores metadata and extracted text content for documents
 * that users upload for AI-powered querying. Supported formats include
 * PDF, DOCX, and TXT files.</p>
 * 
 * <p>The extracted text is processed into chunks and converted to vector
 * embeddings for semantic search capabilities.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Entity
@Table(name = "documents")
public class Document {
    /** Unique identifier for the document. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** System-generated unique filename for storage. */
    @Column(nullable = false)
    private String filename;

    /** Original filename as uploaded by the user. */
    @Column(nullable = false)
    private String originalFilename;

    /** MIME type of the uploaded file. */
    @Column(nullable = false)
    private String contentType;

    /** Size of the uploaded file in bytes. */
    @Column(nullable = false)
    private Long fileSize;

    /** File system path where the document is stored. */
    @Column(nullable = false)
    private String filePath;

    /** Extracted text content from the document. */
    @Column(columnDefinition = "TEXT")
    private String extractedText;

    /** Timestamp when the document was uploaded. */
    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    /**
     * Default constructor that initializes the upload timestamp.
     */
    public Document() {
        this.uploadedAt = LocalDateTime.now();
    }

    // Getters and Setters
    
    /** @return the document ID */
    public Long getId() { return id; }
    
    /** @param id the document ID to set */
    public void setId(Long id) { this.id = id; }

    /** @return the system-generated filename */
    public String getFilename() { return filename; }
    
    /** @param filename the system-generated filename to set */
    public void setFilename(String filename) { this.filename = filename; }

    /** @return the original filename */
    public String getOriginalFilename() { return originalFilename; }
    
    /** @param originalFilename the original filename to set */
    public void setOriginalFilename(String originalFilename) { this.originalFilename = originalFilename; }

    /** @return the content type */
    public String getContentType() { return contentType; }
    
    /** @param contentType the content type to set */
    public void setContentType(String contentType) { this.contentType = contentType; }

    /** @return the file size in bytes */
    public Long getFileSize() { return fileSize; }
    
    /** @param fileSize the file size to set */
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    /** @return the file path */
    public String getFilePath() { return filePath; }
    
    /** @param filePath the file path to set */
    public void setFilePath(String filePath) { this.filePath = filePath; }

    /** @return the extracted text content */
    public String getExtractedText() { return extractedText; }
    
    /** @param extractedText the extracted text to set */
    public void setExtractedText(String extractedText) { this.extractedText = extractedText; }

    /** @return the upload timestamp */
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    
    /** @param uploadedAt the upload timestamp to set */
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}