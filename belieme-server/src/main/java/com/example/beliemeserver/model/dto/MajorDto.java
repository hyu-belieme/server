package com.example.beliemeserver.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class MajorDto {
    private UniversityDto university;
    @NonNull
    private String code;
}
