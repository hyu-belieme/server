package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;

public class SampleData {
    public static final UniversityDto notFoundedUniversity = new UniversityDto("HANYANG", "한양대학교", null);
    public static final DepartmentDto notFoundedDepartment = new DepartmentDto(InitialData.universityDummies.get(0), "COMPUTER", "컴퓨터소프트웨어학부");
    public static final MajorDto notFoundedMajor = new MajorDto(InitialData.universityDummies.get(0), "DOESNT_EXIST");

}
