package com.example.beliemeserver.config.cors.container;

public record CorsRegistrationInfo(
        String mapping,
        String[] allowedOrigins,
        String[] allowedMethods,
        String[] allowedHeaders,
        String[] exposedHeaders,
        Long maxAge,
        Boolean allowCredentials
) {
}
