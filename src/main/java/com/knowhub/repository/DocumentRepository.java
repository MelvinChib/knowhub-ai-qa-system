package com.knowhub.repository;

import com.knowhub.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Document entity operations.
 * 
 * <p>This repository provides data access methods for document entities,
 * extending Spring Data JPA's standard CRUD operations with custom
 * query methods for document management.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    /**
     * Retrieves all documents ordered by upload date in descending order.
     * 
     * @return list of documents sorted by most recent upload first
     */
    List<Document> findAllByOrderByUploadedAtDesc();
}