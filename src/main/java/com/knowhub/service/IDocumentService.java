package com.knowhub.service;

import com.knowhub.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IDocumentService {
    DocumentResponse uploadDocument(MultipartFile file);
    List<DocumentResponse> getAllDocuments();
    void deleteDocument(Long id);
}
