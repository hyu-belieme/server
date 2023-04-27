package com.example.beliemeserver.domain.dto._new;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record DepartmentDto(
        @NonNull UUID id, @NonNull UniversityDto university,
        @NonNull String name, @NonNull List<MajorDto> baseMajors
) {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final DepartmentDto nestedEndpoint = new DepartmentDto(NIL_UUID, UniversityDto.nestedEndpoint, "-", new ArrayList<>());

    public DepartmentDto(@NonNull UUID id, @NonNull UniversityDto university, @NonNull String name, @NonNull List<MajorDto> baseMajors) {
        this.id = id;
        this.university = university;
        this.name = name;
        this.baseMajors = new ArrayList<>(baseMajors);
    }

    public static DepartmentDto init(@NonNull UUID id, @NonNull UniversityDto university, @NonNull String name) {
        return new DepartmentDto(id, university, name, new ArrayList<>());
    }

    @Override
    public List<MajorDto> baseMajors() {
        return new ArrayList<>(baseMajors);
    }

    public DepartmentDto withUniversity(@NonNull UniversityDto university) {
        return new DepartmentDto(id, university, name, baseMajors);
    }

    public DepartmentDto withName(@NonNull String name) {
        return new DepartmentDto(id, university, name, baseMajors);
    }

    public DepartmentDto withBaseMajors(@NonNull List<MajorDto> baseMajors) {
        return new DepartmentDto(id, university, name, baseMajors);
    }

    public DepartmentDto withBaseMajorAdd(MajorDto newMajor) {
        DepartmentDto output = new DepartmentDto(id, university, name, baseMajors);
        output.baseMajors.add(newMajor);

        return output;
    }

    public DepartmentDto withBaseMajorRemove(MajorDto targetMajor) {
        DepartmentDto output = new DepartmentDto(id, university, name, baseMajors);
        output.baseMajors.remove(targetMajor);

        return output;
    }

    public boolean matchUniqueKey(String universityName, String departmentName) {
        return this.university.matchUniqueKey(universityName)
                && this.name.equals(departmentName);
    }

    public boolean matchUniqueKey(DepartmentDto oth) {
        if (oth == null) {
            return false;
        }
        return matchUniqueKey(oth.university.name(), oth.name);
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }
        return "DepartmentDto{" +
                "id='" + id + '\'' +
                ", university=" + university +
                ", name='" + name + '\'' +
                ", baseMajors=" + baseMajors +
                '}';
    }
}
