package com.example.beliemeserver.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class DepartmentDto {
    private UniversityDto university;

    @NonNull
    private String code;

    @NonNull
    private String name;

    private final List<MajorDto> baseMajors = new ArrayList<>();

    public void addBaseMajor(MajorDto major) {
        baseMajors.add(major);
    }
}
