package com.knowhub.controller;

import com.knowhub.constant.AppConstants;
import com.knowhub.dto.ApiResponse;
import com.knowhub.dto.QueryHistoryResponse;
import com.knowhub.dto.QueryRequest;
import com.knowhub.dto.QueryResponse;
import com.knowhub.service.IQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for query processing and history management.
 * 
 * <p>This controller provides endpoints for the core AI functionality:
 * processing user queries using RAG (Retrieval-Augmented Generation)
 * and managing query history for user reference.</p>
 * 
 * <p>The query endpoint implements the complete RAG pipeline, retrieving
 * relevant document chunks and generating contextual AI responses.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@RestController
@RequestMapping(AppConstants.Api.QUERY)
@Tag(name = "Query Management", description = "APIs for querying documents and managing chat history")
public class QueryController {
    
    /** Service for query processing operations. */
    private final IQueryService queryService;

    /**
     * Constructor for QueryController.
     * 
     * @param queryService the query service
     */
    public QueryController(IQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Processes a user question using RAG (Retrieval-Augmented Generation).
     * 
     * <p>This endpoint implements the core AI functionality by:</p>
     * <ul>
     *   <li>Finding relevant document chunks through similarity search</li>
     *   <li>Constructing a contextual prompt</li>
     *   <li>Generating an AI response using the retrieved context</li>
     *   <li>Saving the interaction to history</li>
     * </ul>
     * 
     * @param request the query request containing the user's question
     * @return ResponseEntity containing the AI response and context sources
     */
    @PostMapping
    @Operation(summary = "Ask a question", description = "Ask a question about uploaded documents and get AI-generated answer")
    public ResponseEntity<ApiResponse<QueryResponse>> askQuestion(@Valid @RequestBody QueryRequest request) {
        QueryResponse response = queryService.processQuery(request.question());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Retrieves the recent query history.
     * 
     * <p>Returns the 20 most recent user questions and AI responses
     * for review and reference purposes.</p>
     * 
     * @return ResponseEntity containing list of query history records
     */
    @GetMapping("/history")
    @Operation(summary = "Get query history", description = "Retrieve recent questions and answers")
    public ResponseEntity<ApiResponse<List<QueryHistoryResponse>>> getQueryHistory() {
        List<QueryHistoryResponse> history = queryService.getQueryHistory();
        return ResponseEntity.ok(ApiResponse.success(history));
    }
}