package com.knowhub.constant;

public final class AppConstants {
    
    private AppConstants() {}
    
    public static final class Api {
        public static final String VERSION = "/api/v1";
        public static final String DOCUMENTS = VERSION + "/documents";
        public static final String QUERY = VERSION + "/query";
        public static final String AUTH = VERSION + "/auth";
        
        private Api() {}
    }
    
    public static final class FileType {
        public static final String PDF = "application/pdf";
        public static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        public static final String TXT = "text/plain";
        
        private FileType() {}
    }
    
    public static final class ErrorMessages {
        public static final String FILE_EMPTY = "File is empty";
        public static final String UNSUPPORTED_FILE_TYPE = "Unsupported file type. Only PDF, DOCX, and TXT files are allowed.";
        public static final String DOCUMENT_NOT_FOUND = "Document not found";
        public static final String UPLOAD_FAILED = "Failed to upload document: %s";
        public static final String NO_DOCUMENTS = "I don't have any relevant documents to answer your question. Please upload some documents first.";
        
        private ErrorMessages() {}
    }
    
    public static final class Prompts {
        public static final String SYSTEM_PROMPT = """
            You are a helpful AI assistant that answers questions based on the provided context.
            Use the following context to answer the user's question accurately and concisely.
            If the context doesn't contain enough information to answer the question, say so clearly.
            
            Context:
            {context}
            
            Question: {question}
            
            Answer:
            """;
        
        private Prompts() {}
    }
}
