package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class DepartmentDto {
    @NonNull
    @Setter(AccessLevel.NONE)
    private UniversityDto university;

    @NonNull
    private String code;

    @NonNull
    private String name;

    @NonNull
    @Setter(AccessLevel.NONE)
    private List<MajorDto> baseMajors;

    public DepartmentDto(@NonNull UniversityDto university, @NonNull String code, @NonNull String name) {
        setUniversity(university);
        setCode(code);
        setName(name);
        setBaseMajors(new ArrayList<>());
    }

    public DepartmentDto(@NonNull UniversityDto university, @NonNull String code, @NonNull String name, @NonNull List<MajorDto> baseMajors) {
        setUniversity(university);
        setCode(code);
        setName(name);
        setBaseMajors(baseMajors);
    }

    public DepartmentDto(DepartmentDto departmentDto) {
        this.university = departmentDto.getUniversity();
        this.code = departmentDto.getCode();
        this.name = departmentDto.getName();
        this.baseMajors = departmentDto.getBaseMajors();
    }

    public UniversityDto getUniversity() {
        return new UniversityDto(university);
    }

    public List<MajorDto> getBaseMajors() {
        List<MajorDto> output = new ArrayList<>();
        for(MajorDto major : baseMajors) {
            output.add(new MajorDto(major));
        }

        return output;
    }

    public DepartmentDto setUniversity(@NonNull UniversityDto university) {
        this.university = new UniversityDto(university);
        return this;
    }

    public DepartmentDto setBaseMajors(@NonNull List<MajorDto> baseMajors) {
        this.baseMajors = new ArrayList<>();
        for(MajorDto major : baseMajors) {
            addBaseMajor(new MajorDto(major));
        }
        return this;
    }

    public void addBaseMajor(MajorDto major) {
        baseMajors.add(major);
    }
}
