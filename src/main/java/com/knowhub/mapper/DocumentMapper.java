package com.knowhub.mapper;

import com.knowhub.dto.DocumentResponse;
import com.knowhub.model.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {
    
    public DocumentResponse toResponse(Document document) {
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
