package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.MajorDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DepartmentResponse extends JsonResponse {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private UniversityResponse university;
    private String code;
    private String name;
    private List<String> baseMajors;

    private DepartmentResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private DepartmentResponse(UniversityResponse university, String code, String name, List<String> baseMajors) {
        super(true);
        this.university = university;
        this.code = code;
        this.name = name;
        this.baseMajors = baseMajors;
    }

    public static DepartmentResponse responseWillBeIgnore() {
        return new DepartmentResponse(false);
    }

    public static DepartmentResponse from(DepartmentDto departmentDto) {
        if (departmentDto == null) return null;
        if (departmentDto.equals(DepartmentDto.nestedEndpoint)) {
            return responseWillBeIgnore();
        }

        List<String> baseMajorCodes = new ArrayList<>();
        for (MajorDto major : departmentDto.baseMajors()) {
            baseMajorCodes.add(major.code());
        }

        return new DepartmentResponse(
                UniversityResponse.from(departmentDto.university()),
                departmentDto.code(),
                departmentDto.name(),
                baseMajorCodes
        );
    }

    public DepartmentResponse withoutUniversity() {
        return new DepartmentResponse(
                UniversityResponse.responseWillBeIgnore(),
                code,
                name,
                baseMajors
        );
    }
}
