package com.knowhub.service;

import com.knowhub.dto.DocumentResponse;
import com.knowhub.exception.DocumentProcessingException;
import com.knowhub.model.Document;
import com.knowhub.repository.DocumentRepository;
import com.knowhub.repository.EmbeddingRepository;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import io.micrometer.core.annotation.Timed;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Service class for document management operations.
 * 
 * <p>This service handles document upload, text extraction, storage, and deletion.
 * It supports PDF, DOCX, and TXT file formats and integrates with the embedding
 * service to generate vector embeddings for uploaded documents.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>File upload validation and storage</li>
 *   <li>Text extraction from various document formats</li>
 *   <li>Document metadata management</li>
 *   <li>Integration with embedding generation</li>
 *   <li>Document deletion with cleanup</li>
 * </ul>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Service
public class DocumentService {
    
    /** Repository for document data access. */
    private final DocumentRepository documentRepository;
    
    /** Repository for embedding vector data access. */
    private final EmbeddingRepository embeddingRepository;
    
    /** Service for generating document embeddings. */
    private final EmbeddingService embeddingService;
    
    /** Directory path for storing uploaded files. */
    @Value("${knowhub.upload.directory}")
    private String uploadDirectory;

    /**
     * Constructor for DocumentService.
     * 
     * @param documentRepository repository for document operations
     * @param embeddingRepository repository for embedding operations
     * @param embeddingService service for generating embeddings
     */
    public DocumentService(DocumentRepository documentRepository, 
                          EmbeddingRepository embeddingRepository,
                          EmbeddingService embeddingService) {
        this.documentRepository = documentRepository;
        this.embeddingRepository = embeddingRepository;
        this.embeddingService = embeddingService;
    }

    /**
     * Uploads and processes a document file.
     * 
     * <p>This method validates the file, extracts text content, stores the file
     * and metadata, and triggers embedding generation for semantic search.</p>
     * 
     * @param file the multipart file to upload
     * @return DocumentResponse containing document metadata
     * @throws DocumentProcessingException if file processing fails
     */
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    @Timed(value = "document.upload", description = "Time taken to upload and process documents")
    public DocumentResponse uploadDocument(MultipartFile file) {
        validateFile(file);
        
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDirectory);
            Files.createDirectories(uploadPath);
            
            // Generate unique filename
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(filename);
            
            // Save file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            // Extract text
            String extractedText = extractText(file, filePath);
            
            // Save document metadata
            Document document = new Document();
            document.setFilename(filename);
            document.setOriginalFilename(file.getOriginalFilename());
            document.setContentType(file.getContentType());
            document.setFileSize(file.getSize());
            document.setFilePath(filePath.toString());
            document.setExtractedText(extractedText);
            
            document = documentRepository.save(document);
            
            // Generate and store embeddings asynchronously
            embeddingService.generateEmbeddings(document);
            
            return mapToResponse(document);
            
        } catch (IOException e) {
            throw new DocumentProcessingException("Failed to upload document: " + e.getMessage());
        }
    }

    /**
     * Retrieves all uploaded documents.
     * 
     * @return list of document responses ordered by upload date (newest first)
     */
    @Cacheable(value = "documents")
    @Timed(value = "document.retrieval", description = "Time taken to retrieve documents")
    public List<DocumentResponse> getAllDocuments() {
        return documentRepository.findAllByOrderByUploadedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Deletes a document and all associated data.
     * 
     * <p>This method removes the document file from storage, deletes all
     * associated embedding vectors, and removes the document record from
     * the database.</p>
     * 
     * @param id the ID of the document to delete
     * @throws DocumentProcessingException if document is not found
     */
    @Transactional
    @CacheEvict(value = "documents", allEntries = true)
    @Timed(value = "document.deletion", description = "Time taken to delete documents")
    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new DocumentProcessingException("Document not found"));
        
        // Delete embeddings
        embeddingRepository.deleteByDocumentId(id);
        
        // Delete file
        try {
            Files.deleteIfExists(Paths.get(document.getFilePath()));
        } catch (IOException e) {
            // Log warning but continue with database deletion
        }
        
        // Delete document record
        documentRepository.delete(document);
    }

    /**
     * Validates the uploaded file for supported formats and content.
     * 
     * @param file the file to validate
     * @throws DocumentProcessingException if file is invalid or unsupported
     */
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new DocumentProcessingException("File is empty");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") && 
            !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") &&
            !contentType.equals("text/plain"))) {
            throw new DocumentProcessingException("Unsupported file type. Only PDF, DOCX, and TXT files are allowed.");
        }
    }

    /**
     * Extracts text content from the uploaded file based on its type.
     * 
     * @param file the uploaded file
     * @param filePath the path where the file is stored
     * @return extracted text content
     * @throws IOException if text extraction fails
     */
    private String extractText(MultipartFile file, Path filePath) throws IOException {
        String contentType = file.getContentType();
        
        return switch (contentType) {
            case "application/pdf" -> extractPdfText(filePath);
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> extractDocxText(file);
            case "text/plain" -> new String(file.getBytes());
            default -> throw new DocumentProcessingException("Unsupported file type");
        };
    }

    /**
     * Extracts text from a PDF file using Apache PDFBox.
     * 
     * @param filePath path to the PDF file
     * @return extracted text content
     * @throws IOException if PDF processing fails
     */
    private String extractPdfText(Path filePath) throws IOException {
        try (PDDocument document = Loader.loadPDF(filePath.toFile())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }

    /**
     * Extracts text from a DOCX file using Apache POI.
     * 
     * @param file the DOCX file
     * @return extracted text content
     * @throws IOException if DOCX processing fails
     */
    private String extractDocxText(MultipartFile file) throws IOException {
        try (XWPFDocument document = new XWPFDocument(file.getInputStream());
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
            return extractor.getText();
        }
    }

    /**
     * Maps a Document entity to a DocumentResponse DTO.
     * 
     * @param document the document entity
     * @return document response DTO
     */
    private DocumentResponse mapToResponse(Document document) {
        return new DocumentResponse(
            document.getId(),
            document.getFilename(),
            document.getOriginalFilename(),
            document.getContentType(),
            document.getFileSize(),
            document.getUploadedAt()
        );
    }
}