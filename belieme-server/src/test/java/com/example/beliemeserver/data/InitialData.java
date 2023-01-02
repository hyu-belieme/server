package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.dto.UniversityDto;

import java.util.ArrayList;
import java.util.List;

public class InitialData {
    public static final List<UniversityDto> universityDummies = List.of(
            new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/"),
            new UniversityDto("CKU", "가톨릭관동대학교", null),
            new UniversityDto("SNU", "서울대학교", null)
    );

    public static final List<MajorDto> majorDummies = List.of(
            new MajorDto(universityDummies.get(0), "FH04067"),
            new MajorDto(universityDummies.get(0), "FH04068"),
            new MajorDto(universityDummies.get(0), "FH04069"),
            new MajorDto(universityDummies.get(1), "TEST"),
            new MajorDto(universityDummies.get(0), "TEST"),
            new MajorDto(universityDummies.get(1), "A68"),
            new MajorDto(universityDummies.get(1), "A69"),
            new MajorDto(universityDummies.get(1), "A70")
    );

    private static final List<MajorDto> HYU_CSE_BASE_MAJORS = List.of(
            majorDummies.get(0),
            majorDummies.get(1)
    );

    private static final List<MajorDto> HYU_STU_BASE_MAJORS = List.of(
            majorDummies.get(0),
            majorDummies.get(1),
            majorDummies.get(2),
            majorDummies.get(4)
    );

    private static final List<MajorDto> CKU_MED_BASE_MAJORS = List.of(
            majorDummies.get(5)
    );

    private static final List<MajorDto> CKU_STU_BASE_MAJORS = List.of(
            majorDummies.get(3),
            majorDummies.get(5),
            majorDummies.get(6),
            majorDummies.get(7)
    );

    public static final List<DepartmentDto> departmentDummies = List.of(
            new DepartmentDto(universityDummies.get(0), "CSE", "컴퓨터소프트웨어학부", HYU_CSE_BASE_MAJORS),
            new DepartmentDto(universityDummies.get(0), "STU", "총학생회", HYU_STU_BASE_MAJORS),
            new DepartmentDto(universityDummies.get(1), "MED", "의과대학", CKU_MED_BASE_MAJORS),
            new DepartmentDto(universityDummies.get(1), "STU", "총학생회", CKU_STU_BASE_MAJORS)
    );

    public static List<UniversityDto> getUniversityDummies() {
        return new ArrayList<>(universityDummies);
    }

    public static List<MajorDto> getMajorDummies() {
        return new ArrayList<>(majorDummies);
    }

    public static List<DepartmentDto> getDepartmentDummies() {
        return new ArrayList<>(departmentDummies);
    }

    public static UniversityDto getUniversityDummy(String universityCode) {
        for(UniversityDto universityDto : universityDummies) {
            if(universityCode.equals(universityDto.getCode())) {
                return universityDto;
            }
        }
        return null;
    }

    public static MajorDto getMajorDummy(String universityCode, String majorCode) {
        for(MajorDto majorDto : majorDummies) {
            if(!universityCode.equals(majorDto.getUniversity().getCode())) {
                continue;
            }
            if(majorCode.equals(majorDto.getCode())) {
                return majorDto;
            }
        }
        return null;
    }

    public static DepartmentDto getDepartmentDummy(String universityCode, String departmentCode) {
        for(DepartmentDto departmentDto : departmentDummies) {
            if(!universityCode.equals(departmentDto.getUniversity().getCode())) {
                continue;
            }
            if(departmentCode.equals(departmentDto.getCode())) {
                return departmentDto;
            }
        }
        return null;
    }
}