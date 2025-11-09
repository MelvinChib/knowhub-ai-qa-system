package com.knowhub.validation;

import com.knowhub.constant.AppConstants;
import com.knowhub.exception.DocumentProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class FileValidator {
    
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
        AppConstants.FileType.PDF,
        AppConstants.FileType.DOCX,
        AppConstants.FileType.TXT
    );
    
    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DocumentProcessingException(AppConstants.ErrorMessages.FILE_EMPTY);
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new DocumentProcessingException(AppConstants.ErrorMessages.UNSUPPORTED_FILE_TYPE);
        }
    }
}
