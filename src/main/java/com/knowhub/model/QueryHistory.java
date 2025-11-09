package com.knowhub.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing the history of user queries and AI responses.
 * 
 * <p>This entity stores the complete conversation history including
 * user questions, AI-generated answers, and the context documents
 * that were used to generate each response.</p>
 * 
 * <p>This enables users to review their previous interactions and
 * provides audit trail for the AI system's responses.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Entity
@Table(name = "query_history")
public class QueryHistory {
    /** Unique identifier for the query history record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The user's question. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    /** The AI-generated answer. */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String answer;

    /** List of document names that provided context for the answer. */
    @ElementCollection
    @CollectionTable(name = "query_context_docs", joinColumns = @JoinColumn(name = "query_id"))
    @Column(name = "document_name")
    private List<String> contextDocuments;

    /** Timestamp when the query was processed. */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor that initializes the creation timestamp.
     */
    public QueryHistory() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    
    /** @return the query history ID */
    public Long getId() { return id; }
    
    /** @param id the query history ID to set */
    public void setId(Long id) { this.id = id; }

    /** @return the user's question */
    public String getQuestion() { return question; }
    
    /** @param question the user's question to set */
    public void setQuestion(String question) { this.question = question; }

    /** @return the AI-generated answer */
    public String getAnswer() { return answer; }
    
    /** @param answer the AI-generated answer to set */
    public void setAnswer(String answer) { this.answer = answer; }

    /** @return the list of context document names */
    public List<String> getContextDocuments() { return contextDocuments; }
    
    /** @param contextDocuments the list of context document names to set */
    public void setContextDocuments(List<String> contextDocuments) { this.contextDocuments = contextDocuments; }

    /** @return the creation timestamp */
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    /** @param createdAt the creation timestamp to set */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}