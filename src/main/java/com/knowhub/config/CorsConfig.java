package com.knowhub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 * 
 * <p>This configuration enables the backend to accept requests from the
 * React frontend running on localhost:5173, allowing seamless integration
 * between the frontend and backend components.</p>
 * 
 * @author KnowHub Development Team
 * @version 1.0.0
 * @since 2024
 */
@Configuration
public class CorsConfig {

    /**
     * Configures CORS settings for the application.
     * 
     * <p>Allows requests from the React frontend on localhost:5173
     * with all necessary HTTP methods and headers.</p>
     * 
     * @return configured CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}