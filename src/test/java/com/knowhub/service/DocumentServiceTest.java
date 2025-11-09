package com.knowhub.service;

import com.knowhub.dto.DocumentResponse;
import com.knowhub.mapper.DocumentMapper;
import com.knowhub.model.Document;
import com.knowhub.repository.DocumentRepository;
import com.knowhub.repository.EmbeddingRepository;
import com.knowhub.validation.FileValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    
    @Mock
    private DocumentRepository documentRepository;
    
    @Mock
    private EmbeddingRepository embeddingRepository;
    
    @Mock
    private IEmbeddingService embeddingService;
    
    @Mock
    private FileValidator fileValidator;
    
    @Mock
    private DocumentMapper documentMapper;
    
    @InjectMocks
    private DocumentService documentService;
    
    @Test
    void getAllDocuments_shouldReturnDocumentList() {
        Document doc = new Document();
        doc.setId(1L);
        doc.setFilename("test.pdf");
        
        DocumentResponse response = new DocumentResponse(1L, "test.pdf", "test.pdf", "application/pdf", 1000L, null);
        
        when(documentRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(doc));
        when(documentMapper.toResponse(doc)).thenReturn(response);
        
        List<DocumentResponse> result = documentService.getAllDocuments();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(documentRepository).findAllByOrderByCreatedAtDesc();
    }
}
