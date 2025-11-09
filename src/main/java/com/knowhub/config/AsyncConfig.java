package com.knowhub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class for enabling asynchronous processing.
 * 
 * <p>This configuration enables Spring's asynchronous method execution
 * capability, which is used for background processing of document
 * embeddings to improve response times for document uploads.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}