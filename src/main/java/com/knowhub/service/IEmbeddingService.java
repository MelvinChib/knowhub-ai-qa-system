package com.knowhub.service;

import com.knowhub.model.Document;
import com.knowhub.model.EmbeddingVector;

import java.util.List;

public interface IEmbeddingService {
    void generateEmbeddings(Document document);
    List<EmbeddingVector> findSimilarChunks(String query, int limit);
}
