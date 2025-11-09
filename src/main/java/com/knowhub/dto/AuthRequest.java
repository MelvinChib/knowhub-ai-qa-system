package com.knowhub.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Authentication request DTO.
 * 
 * @param username the username
 * @param password the password
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public record AuthRequest(
    @NotBlank(message = "Username is required")
    String username,
    
    @NotBlank(message = "Password is required")
    String password
) {}