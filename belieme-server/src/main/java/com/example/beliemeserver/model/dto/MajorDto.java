package com.example.beliemeserver.model.dto;

import lombok.NonNull;

public record MajorDto(@NonNull UniversityDto university, @NonNull String code) {
    public static final MajorDto nestedEndpoint = new MajorDto(UniversityDto.nestedEndpoint, "-");

    public MajorDto withUniversity(@NonNull UniversityDto university) {
        return new MajorDto(university, code);
    }

    public MajorDto withCode(@NonNull String code) {
        return new MajorDto(university, code);
    }
}
