package com.knowhub.repository;

import com.knowhub.model.EmbeddingVector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for EmbeddingVector entity operations.
 * 
 * <p>This repository provides data access methods for vector embeddings,
 * including specialized methods for similarity search using PostgreSQL's
 * pgvector extension. The similarity search is crucial for the RAG
 * (Retrieval-Augmented Generation) functionality.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Repository
public interface EmbeddingRepository extends JpaRepository<EmbeddingVector, Long> {
    
    /**
     * Finds the most similar embedding vectors to a query embedding.
     * 
     * <p>Uses PostgreSQL's pgvector cosine distance operator (<->) to find
     * the most semantically similar document chunks to the query.</p>
     * 
     * @param queryEmbedding the query embedding vector as a string
     * @param limit maximum number of similar vectors to return
     * @return list of embedding vectors ordered by similarity (most similar first)
     */
    @Query(value = """
        SELECT e.* FROM embedding_vectors e 
        ORDER BY e.embedding_vector <-> CAST(:queryEmbedding AS vector) 
        LIMIT :limit
        """, nativeQuery = true)
    List<EmbeddingVector> findTopSimilar(@Param("queryEmbedding") String queryEmbedding, @Param("limit") int limit);
    
    /**
     * Deletes all embedding vectors associated with a specific document.
     * 
     * @param documentId the ID of the document whose embeddings should be deleted
     */
    void deleteByDocumentId(Long documentId);
}