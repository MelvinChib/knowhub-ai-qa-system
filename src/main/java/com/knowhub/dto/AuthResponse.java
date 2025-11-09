package com.knowhub.dto;

/**
 * Authentication response DTO.
 * 
 * @param token the JWT token
 * @param username the username
 * @param role the user role
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
public record AuthResponse(
    String token,
    String username,
    String role
) {}