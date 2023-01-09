package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class MajorDto {
    @NonNull
    @Setter(AccessLevel.NONE)
    private UniversityDto university;

    @NonNull
    private String code;

    public MajorDto(@NonNull UniversityDto university, String code) {
        setUniversity(university);
        setCode(code);
    }

    public MajorDto(@NonNull MajorDto majorDto) {
        this.university = majorDto.getUniversity();
        this.code = majorDto.getCode();
    }

    public UniversityDto getUniversity() {
        return new UniversityDto(university);
    }

    public MajorDto setUniversity(@NonNull UniversityDto university) {
        this.university = new UniversityDto(university);
        return this;
    }
}
