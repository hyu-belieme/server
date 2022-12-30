package com.example.beliemeserver.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UniversityDto {
    @NonNull
    private String code;

    @NonNull
    private String name;

    private String apiUrl;
}