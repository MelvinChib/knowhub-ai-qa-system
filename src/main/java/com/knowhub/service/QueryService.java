package com.knowhub.service;

import com.knowhub.dto.QueryResponse;
import com.knowhub.model.EmbeddingVector;
import com.knowhub.model.QueryHistory;
import com.knowhub.repository.QueryHistoryRepository;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;
import io.micrometer.core.annotation.Timed;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class implementing RAG (Retrieval-Augmented Generation) for query processing.
 * 
 * <p>This service handles user queries by implementing a complete RAG pipeline:
 * retrieving relevant document chunks through similarity search, augmenting
 * the query with contextual information, and generating AI responses using
 * the retrieved context.</p>
 * 
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Query processing with context retrieval</li>
 *   <li>Prompt engineering for contextual AI responses</li>
 *   <li>Integration with OpenAI's chat models</li>
 *   <li>Query history management</li>
 * </ul>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Service
public class QueryService {
    
    /** Spring AI chat model for generating responses. */
    private final ChatModel chatModel;
    
    /** Service for embedding operations and similarity search. */
    private final EmbeddingService embeddingService;
    
    /** Repository for storing query history. */
    private final QueryHistoryRepository queryHistoryRepository;
    
    /** System prompt template for contextual AI responses. */
    private static final String SYSTEM_PROMPT = """
        You are a helpful AI assistant that answers questions based on the provided context.
        Use the following context to answer the user's question accurately and concisely.
        If the context doesn't contain enough information to answer the question, say so clearly.
        
        Context:
        {context}
        
        Question: {question}
        
        Answer:
        """;

    /**
     * Constructor for QueryService.
     * 
     * @param chatModel the Spring AI chat model
     * @param embeddingService service for embedding operations
     * @param queryHistoryRepository repository for query history
     */
    public QueryService(ChatModel chatModel, 
                       EmbeddingService embeddingService,
                       QueryHistoryRepository queryHistoryRepository) {
        this.chatModel = chatModel;
        this.embeddingService = embeddingService;
        this.queryHistoryRepository = queryHistoryRepository;
    }

    /**
     * Processes a user query using RAG (Retrieval-Augmented Generation).
     * 
     * <p>This method implements the complete RAG pipeline:</p>
     * <ol>
     *   <li>Retrieves similar document chunks using embedding similarity</li>
     *   <li>Constructs a contextual prompt with retrieved information</li>
     *   <li>Generates an AI response using the chat model</li>
     *   <li>Saves the interaction to query history</li>
     * </ol>
     * 
     * @param question the user's question
     * @return QueryResponse containing the AI answer and context sources
     */
    @Timed(value = "query.processing", description = "Time taken to process queries")
    public QueryResponse processQuery(String question) {
        // Find similar document chunks
        List<EmbeddingVector> similarChunks = embeddingService.findSimilarChunks(question, 5);
        
        if (similarChunks.isEmpty()) {
            String noContextAnswer = "I don't have any relevant documents to answer your question. Please upload some documents first.";
            saveQueryHistory(question, noContextAnswer, List.of());
            return new QueryResponse(noContextAnswer, List.of());
        }
        
        // Build context from similar chunks
        String context = similarChunks.stream()
                .map(EmbeddingVector::getContent)
                .collect(Collectors.joining("\n\n"));
        
        // Get document names for response
        List<String> contextDocuments = similarChunks.stream()
                .map(chunk -> chunk.getDocument().getOriginalFilename())
                .distinct()
                .toList();
        
        // Create prompt
        PromptTemplate promptTemplate = new PromptTemplate(SYSTEM_PROMPT);
        Prompt prompt = promptTemplate.create(Map.of(
            "context", context,
            "question", question
        ));
        
        // Get AI response
        String answer = chatModel.call(prompt).getResult().getOutput().getContent();
        
        // Save to history
        saveQueryHistory(question, answer, contextDocuments);
        
        return new QueryResponse(answer, contextDocuments);
    }
    
    /**
     * Retrieves the recent query history.
     * 
     * @return list of the 20 most recent query history records
     */
    public List<QueryHistory> getQueryHistory() {
        return queryHistoryRepository.findTop20ByOrderByCreatedAtDesc();
    }
    
    /**
     * Saves a query interaction to the history.
     * 
     * @param question the user's question
     * @param answer the AI-generated answer
     * @param contextDocuments list of documents that provided context
     */
    private void saveQueryHistory(String question, String answer, List<String> contextDocuments) {
        QueryHistory history = new QueryHistory();
        history.setQuestion(question);
        history.setAnswer(answer);
        history.setContextDocuments(contextDocuments);
        queryHistoryRepository.save(history);
    }
}