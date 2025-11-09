package com.knowhub.dto;

import java.util.List;

/**
 * Data Transfer Object for AI query responses.
 * 
 * <p>This record represents the AI system's response to a user query,
 * including the generated answer and the list of documents that were
 * used as context for generating the response.</p>
 * 
 * @param answer the AI-generated answer to the user's question
 * @param contextDocuments list of document names that provided context
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public record QueryResponse(
    String answer,
    List<String> contextDocuments
) {}