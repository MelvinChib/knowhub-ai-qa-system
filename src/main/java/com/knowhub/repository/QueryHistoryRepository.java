package com.knowhub.repository;

import com.knowhub.model.QueryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for QueryHistory entity operations.
 * 
 * <p>This repository provides data access methods for query history entities,
 * enabling retrieval of user interaction history for audit and review purposes.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Repository
public interface QueryHistoryRepository extends JpaRepository<QueryHistory, Long> {
    
    /**
     * Retrieves the 20 most recent query history records.
     * 
     * @return list of the 20 most recent queries ordered by creation time (newest first)
     */
    List<QueryHistory> findTop20ByOrderByCreatedAtDesc();
}