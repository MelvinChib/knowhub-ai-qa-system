package com.knowhub.service;

import com.knowhub.model.Document;
import com.knowhub.model.EmbeddingVector;
import com.knowhub.repository.EmbeddingRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for generating and managing document embeddings.
 * 
 * <p>This service handles the creation of vector embeddings from document text
 * using OpenAI's embedding model. It chunks documents into smaller pieces,
 * generates embeddings for each chunk, and stores them for similarity search.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Text chunking with configurable size and overlap</li>
 *   <li>Vector embedding generation using Spring AI</li>
 *   <li>Embedding storage in PostgreSQL with pgvector</li>
 *   <li>Similarity search for RAG functionality</li>
 * </ul>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Service
public class EmbeddingService {
    
    /** Spring AI embedding model for generating vector embeddings. */
    private final EmbeddingModel embeddingModel;
    
    /** Repository for storing and retrieving embedding vectors. */
    private final EmbeddingRepository embeddingRepository;
    
    /** Maximum size of text chunks for embedding generation. */
    @Value("${knowhub.embedding.chunk-size:1000}")
    private int chunkSize;
    
    /** Number of characters to overlap between adjacent chunks. */
    @Value("${knowhub.embedding.chunk-overlap:200}")
    private int chunkOverlap;

    /**
     * Constructor for EmbeddingService.
     * 
     * @param embeddingModel the Spring AI embedding model
     * @param embeddingRepository repository for embedding operations
     */
    public EmbeddingService(EmbeddingModel embeddingModel, EmbeddingRepository embeddingRepository) {
        this.embeddingModel = embeddingModel;
        this.embeddingRepository = embeddingRepository;
    }

    /**
     * Generates embeddings for a document asynchronously.
     * 
     * <p>This method chunks the document text, generates vector embeddings
     * for each chunk using OpenAI's embedding model, and stores them in
     * the database for similarity search.</p>
     * 
     * @param document the document to generate embeddings for
     */
    @Async
    public void generateEmbeddings(Document document) {
        String text = document.getExtractedText();
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        List<String> chunks = chunkText(text);
        List<EmbeddingVector> embeddings = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            String chunk = chunks.get(i);
            
            // Generate embedding
            var embeddingResponse = embeddingModel.embedForResponse(List.of(chunk));
            float[] embeddingArray = embeddingResponse.getResults().get(0).getOutput();
            
            // Convert to PostgreSQL vector format
            String embeddingVector = arrayToVectorString(embeddingArray);
            
            EmbeddingVector embedding = new EmbeddingVector(document, chunk, embeddingVector, i);
            embeddings.add(embedding);
        }

        embeddingRepository.saveAll(embeddings);
    }

    /**
     * Finds document chunks similar to a query string.
     * 
     * <p>This method generates an embedding for the query and performs
     * similarity search against stored document embeddings to find the
     * most relevant chunks for RAG context.</p>
     * 
     * @param query the search query
     * @param limit maximum number of similar chunks to return
     * @return list of embedding vectors ordered by similarity
     */
    public List<EmbeddingVector> findSimilarChunks(String query, int limit) {
        // Generate embedding for query
        var embeddingResponse = embeddingModel.embedForResponse(List.of(query));
        float[] queryEmbedding = embeddingResponse.getResults().get(0).getOutput();
        
        String queryVector = arrayToVectorString(queryEmbedding);
        return embeddingRepository.findTopSimilar(queryVector, limit);
    }

    /**
     * Chunks text into smaller pieces with configurable overlap.
     * 
     * <p>This method splits text into chunks based on sentence boundaries
     * while maintaining context through overlapping content between chunks.</p>
     * 
     * @param text the text to chunk
     * @return list of text chunks
     */
    private List<String> chunkText(String text) {
        List<String> chunks = new ArrayList<>();
        String[] sentences = text.split("\\. ");
        
        StringBuilder currentChunk = new StringBuilder();
        
        for (String sentence : sentences) {
            if (currentChunk.length() + sentence.length() > chunkSize && !currentChunk.isEmpty()) {
                chunks.add(currentChunk.toString().trim());
                
                // Handle overlap
                String[] words = currentChunk.toString().split("\\s+");
                currentChunk = new StringBuilder();
                if (words.length > chunkOverlap / 10) { // Approximate word overlap
                    int startIndex = Math.max(0, words.length - chunkOverlap / 10);
                    for (int i = startIndex; i < words.length; i++) {
                        currentChunk.append(words[i]).append(" ");
                    }
                }
            }
            currentChunk.append(sentence).append(". ");
        }
        
        if (!currentChunk.isEmpty()) {
            chunks.add(currentChunk.toString().trim());
        }
        
        return chunks;
    }

    /**
     * Converts a float array to PostgreSQL vector string format.
     * 
     * <p>Transforms the embedding array into the string format required
     * by PostgreSQL's vector data type for storage and similarity search.</p>
     * 
     * @param array the float array to convert
     * @return string representation in PostgreSQL vector format
     */
    private String arrayToVectorString(float[] array) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}