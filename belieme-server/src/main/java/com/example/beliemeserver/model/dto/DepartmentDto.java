package com.example.beliemeserver.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class DepartmentDto {
    private UniversityDto university;
    private String code;
    private String name;
    private List<MajorDto> baseMajors;
}
