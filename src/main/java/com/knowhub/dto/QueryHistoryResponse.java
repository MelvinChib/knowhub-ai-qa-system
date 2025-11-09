package com.knowhub.dto;

import java.time.LocalDateTime;
import java.util.List;

public record QueryHistoryResponse(
    Long id,
    String question,
    String answer,
    List<String> contextDocuments,
    LocalDateTime createdAt
) {}
