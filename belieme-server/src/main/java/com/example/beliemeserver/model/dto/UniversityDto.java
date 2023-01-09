package com.example.beliemeserver.model.dto;

import lombok.NonNull;

public record UniversityDto(@NonNull String code, @NonNull String name, String apiUrl) {
    public static final UniversityDto nestedEndpoint = new UniversityDto("-", "-", "-");

    public UniversityDto withCode(@NonNull String code) {
        return new UniversityDto(code, name, apiUrl);
    }

    public UniversityDto withName(@NonNull String name) {
        return new UniversityDto(code, name, apiUrl);
    }

    public UniversityDto withApiUrl(String apiUrl) {
        return new UniversityDto(code, name, apiUrl);
    }
}