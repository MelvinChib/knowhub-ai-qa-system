package com.knowhub.controller;

import com.knowhub.dto.DocumentResponse;
import com.knowhub.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for document management operations.
 * 
 * <p>This controller provides endpoints for uploading, retrieving, and deleting
 * documents in the intelligent Q&A system. It supports PDF, DOCX, and TXT file formats
 * and integrates with the document processing pipeline.</p>
 * 
 * <p>All endpoints are configured with CORS support for the React frontend
 * running on localhost:5173.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Document Management", description = "APIs for document upload and management")
public class DocumentController {
    
    /** Service for document operations. */
    private final DocumentService documentService;

    /**
     * Constructor for DocumentController.
     * 
     * @param documentService the document service
     */
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Uploads a document file for processing.
     * 
     * <p>Accepts PDF, DOCX, or TXT files, extracts text content,
     * and generates embeddings for semantic search.</p>
     * 
     * @param file the document file to upload
     * @return ResponseEntity containing document metadata
     */
    @PostMapping("/upload")
    @Operation(summary = "Upload a document", description = "Upload a PDF, DOCX, or TXT document for processing")
    public ResponseEntity<DocumentResponse> uploadDocument(@RequestParam("file") MultipartFile file) {
        DocumentResponse response = documentService.uploadDocument(file);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all uploaded documents.
     * 
     * @return ResponseEntity containing list of document metadata
     */
    @GetMapping
    @Operation(summary = "Get all documents", description = "Retrieve list of all uploaded documents")
    public ResponseEntity<List<DocumentResponse>> getAllDocuments() {
        List<DocumentResponse> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }

    /**
     * Deletes a document and all associated data.
     * 
     * <p>Removes the document file, embeddings, and database records.</p>
     * 
     * @param id the ID of the document to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a document", description = "Delete a document and its associated embeddings")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}