package com.knowhub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knowhub.dto.QueryRequest;
import com.knowhub.dto.QueryResponse;
import com.knowhub.service.QueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QueryController.class)
class QueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void askQuestion_ShouldReturnQueryResponse() throws Exception {
        // Given
        QueryRequest request = new QueryRequest("What is AI?");
        QueryResponse response = new QueryResponse("AI is artificial intelligence", List.of("document1.pdf"));
        
        when(queryService.processQuery(anyString())).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("AI is artificial intelligence"))
                .andExpect(jsonPath("$.contextDocuments[0]").value("document1.pdf"));
    }

    @Test
    void askQuestion_WithEmptyQuestion_ShouldReturnBadRequest() throws Exception {
        // Given
        QueryRequest request = new QueryRequest("");

        // When & Then
        mockMvc.perform(post("/api/query")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}