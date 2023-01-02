package com.example.beliemeserver.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DepartmentDto {
    private UniversityDto university;

    @NonNull
    private String code;

    @NonNull
    private String name;

    private final List<MajorDto> baseMajors;

    public DepartmentDto(UniversityDto university, String code, String name) {
        this.university = university;
        this.code = code;
        this.name = name;
        this.baseMajors = new ArrayList<>();
    }

    public DepartmentDto(UniversityDto university, String code, String name, List<MajorDto> baseMajors) {
        this.university = university;
        this.code = code;
        this.name = name;
        this.baseMajors = new ArrayList<>(baseMajors);
    }

    public void addBaseMajor(MajorDto major) {
        baseMajors.add(major);
    }
}
