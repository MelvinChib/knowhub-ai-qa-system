package com.knowhub.mapper;

import com.knowhub.dto.QueryHistoryResponse;
import com.knowhub.model.QueryHistory;
import org.springframework.stereotype.Component;

@Component
public class QueryHistoryMapper {
    
    public QueryHistoryResponse toResponse(QueryHistory queryHistory) {
        return new QueryHistoryResponse(
            queryHistory.getId(),
            queryHistory.getQuestion(),
            queryHistory.getAnswer(),
            queryHistory.getContextDocuments(),
            queryHistory.getCreatedAt()
        );
    }
}
