package com.example.beliemeserver.model.dto;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public record DepartmentDto(
        @NonNull UniversityDto university, @NonNull String code,
        @NonNull String name, @NonNull List<MajorDto> baseMajors
) {
    public static final DepartmentDto nestedEndpoint = new DepartmentDto(UniversityDto.nestedEndpoint, "-", "-", new ArrayList<>());

    public DepartmentDto(@NonNull UniversityDto university, @NonNull String code, @NonNull String name, @NonNull List<MajorDto> baseMajors) {
        this.university = university;
        this.code = code;
        this.name = name;
        this.baseMajors = new ArrayList<>(baseMajors);
    }

    public static DepartmentDto init(@NonNull UniversityDto university, @NonNull String code, @NonNull String name) {
        return new DepartmentDto(university, code, name, new ArrayList<>());
    }

    @Override
    public List<MajorDto> baseMajors() {
        return new ArrayList<>(baseMajors);
    }

    public DepartmentDto withUniversity(@NonNull UniversityDto university) {
        return new DepartmentDto(university, code, name, baseMajors);
    }

    public DepartmentDto withCode(@NonNull String code) {
        return new DepartmentDto(university, code, name, baseMajors);
    }

    public DepartmentDto withName(@NonNull String name) {
        return new DepartmentDto(university, code, name, baseMajors);
    }

    public DepartmentDto withBaseMajors(@NonNull List<MajorDto> baseMajors) {
        return new DepartmentDto(university, code, name, baseMajors);
    }

    public DepartmentDto withBaseMajorAdd(MajorDto newMajor) {
        DepartmentDto output = new DepartmentDto(university, code, name, baseMajors);
        output.baseMajors.add(newMajor);

        return output;
    }

    public DepartmentDto withBaseMajorRemove(MajorDto targetMajor) {
        DepartmentDto output = new DepartmentDto(university, code, name, baseMajors);
        output.baseMajors.remove(targetMajor);

        return output;
    }

    @Override
    public String toString() {
        if(this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "DepartmentDto{" +
                "university=" + university +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", baseMajors=" + baseMajors +
                '}';
    }
}
