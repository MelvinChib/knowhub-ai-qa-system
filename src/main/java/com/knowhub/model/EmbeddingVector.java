package com.knowhub.model;

import jakarta.persistence.*;

/**
 * Entity representing vector embeddings for document text chunks.
 * 
 * <p>This entity stores vector embeddings generated from document text chunks
 * using OpenAI's embedding model. Each embedding represents a semantic vector
 * that enables similarity search for RAG (Retrieval-Augmented Generation).</p>
 * 
 * <p>The embeddings are stored in PostgreSQL using the pgvector extension
 * for efficient similarity search operations.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Entity
@Table(name = "embedding_vectors")
public class EmbeddingVector {
    /** Unique identifier for the embedding vector. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Reference to the source document. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    /** Text content of the document chunk. */
    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** Vector embedding as a PostgreSQL vector type. */
    @Column(name = "embedding_vector", columnDefinition = "vector(1536)")
    private String embedding;

    /** Index of this chunk within the document. */
    @Column(name = "chunk_index")
    private Integer chunkIndex;

    /**
     * Default constructor for JPA.
     */
    public EmbeddingVector() {}

    /**
     * Constructor to create a new embedding vector.
     * 
     * @param document the source document
     * @param content the text content of the chunk
     * @param embedding the vector embedding
     * @param chunkIndex the index of this chunk
     */
    public EmbeddingVector(Document document, String content, String embedding, Integer chunkIndex) {
        this.document = document;
        this.content = content;
        this.embedding = embedding;
        this.chunkIndex = chunkIndex;
    }

    // Getters and Setters
    
    /** @return the embedding vector ID */
    public Long getId() { return id; }
    
    /** @param id the embedding vector ID to set */
    public void setId(Long id) { this.id = id; }

    /** @return the source document */
    public Document getDocument() { return document; }
    
    /** @param document the source document to set */
    public void setDocument(Document document) { this.document = document; }

    /** @return the text content */
    public String getContent() { return content; }
    
    /** @param content the text content to set */
    public void setContent(String content) { this.content = content; }

    /** @return the vector embedding */
    public String getEmbedding() { return embedding; }
    
    /** @param embedding the vector embedding to set */
    public void setEmbedding(String embedding) { this.embedding = embedding; }

    /** @return the chunk index */
    public Integer getChunkIndex() { return chunkIndex; }
    
    /** @param chunkIndex the chunk index to set */
    public void setChunkIndex(Integer chunkIndex) { this.chunkIndex = chunkIndex; }
}