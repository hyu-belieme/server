package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class UniversityDto {
    @NonNull
    private String code;

    @NonNull
    private String name;

    private String apiUrl;

    public UniversityDto(@NonNull UniversityDto universityDto) {
        this.code = universityDto.getCode();
        this.name = universityDto.getName();
        this.apiUrl = universityDto.getApiUrl();
    }
}