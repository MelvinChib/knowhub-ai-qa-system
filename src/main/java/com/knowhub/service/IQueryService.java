package com.knowhub.service;

import com.knowhub.dto.QueryHistoryResponse;
import com.knowhub.dto.QueryResponse;

import java.util.List;

public interface IQueryService {
    QueryResponse processQuery(String question);
    List<QueryHistoryResponse> getQueryHistory();
}
