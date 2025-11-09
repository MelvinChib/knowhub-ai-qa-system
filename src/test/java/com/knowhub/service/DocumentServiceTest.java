package com.knowhub.service;

import com.knowhub.exception.DocumentProcessingException;
import com.knowhub.model.Document;
import com.knowhub.repository.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private EmbeddingService embeddingService;

    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private DocumentService documentService;

    @Test
    void uploadDocument_WithEmptyFile_ShouldThrowException() {
        // Given
        when(multipartFile.isEmpty()).thenReturn(true);

        // When & Then
        assertThrows(DocumentProcessingException.class, 
            () -> documentService.uploadDocument(multipartFile));
    }

    @Test
    void uploadDocument_WithUnsupportedFileType_ShouldThrowException() {
        // Given
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getContentType()).thenReturn("image/jpeg");

        // When & Then
        assertThrows(DocumentProcessingException.class, 
            () -> documentService.uploadDocument(multipartFile));
    }
}