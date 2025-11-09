package com.knowhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI-powered intelligent Q&A system leveraging embeddings, RAG, and Spring AI backend.
 * 
 * <p>This is the main entry point for a sophisticated intelligent question-answering system built with 
 * Spring Boot 3.3+ and Spring AI. The application implements a complete RAG (Retrieval-Augmented Generation) 
 * pipeline that transforms static documents into an interactive, AI-powered knowledge base.</p>
 * 
 * <p>The system architecture includes:</p>
 * <ul>
 *   <li><strong>Document Processing Pipeline:</strong> Multi-format text extraction with intelligent chunking</li>
 *   <li><strong>Vector Embedding Layer:</strong> OpenAI text-embedding-3-small for semantic understanding</li>
 *   <li><strong>Similarity Search Engine:</strong> PostgreSQL pgvector for high-performance vector operations</li>
 *   <li><strong>AI Response Generation:</strong> GPT-4o-mini with context-aware prompt engineering</li>
 *   <li><strong>Conversation Management:</strong> Persistent query history with source attribution</li>
 *   <li><strong>Enterprise APIs:</strong> RESTful endpoints with comprehensive documentation and validation</li>
 * </ul>
 * 
 * <p>Technical highlights:</p>
 * <ul>
 *   <li>Asynchronous document processing for optimal performance</li>
 *   <li>Configurable text chunking with overlap for context preservation</li>
 *   <li>Vector similarity search with cosine distance optimization</li>
 *   <li>Production-ready error handling and input validation</li>
 *   <li>Docker containerization and environment-based configuration</li>
 * </ul>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@SpringBootApplication
public class KnowHubApplication {
    
    /**
     * Main method to start the AI-powered intelligent Q&A system.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(KnowHubApplication.class, args);
    }
}