package com.knowhub.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for user query requests.
 * 
 * <p>This record represents a user's question submitted to the AI system
 * for processing. The question is validated to ensure it's not empty.</p>
 * 
 * @param question the user's question (must not be blank)
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public record QueryRequest(
    @NotBlank(message = "Question cannot be empty")
    String question
) {}