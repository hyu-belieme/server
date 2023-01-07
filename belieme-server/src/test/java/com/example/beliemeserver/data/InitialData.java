package com.example.beliemeserver.data;

import com.example.beliemeserver.model.dto.*;

import java.util.ArrayList;
import java.util.List;

public class InitialData {
    public static List<UniversityDto> universityDummies;
    public static List<MajorDto> majorDummies;
    public static List<DepartmentDto> departmentDummies;
    public static List<UserDto> userDummies;
    public static List<AuthorityDto> authorityDummies;
    public static List<StuffDto> stuffDummies;
    public static List<ItemDto> itemDummies;
    public static List<HistoryDto> historyDummies;

    public static void init() {
        universityDummies = List.of(
                new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/"),
                new UniversityDto("CKU", "가톨릭관동대학교", null),
                new UniversityDto("SNU", "서울대학교", null)
        );

        majorDummies = List.of(
                new MajorDto(universityDummies.get(0), "FH04067"),
                new MajorDto(universityDummies.get(0), "FH04068"),
                new MajorDto(universityDummies.get(0), "FH04069"),
                new MajorDto(universityDummies.get(1), "TEST"),
                new MajorDto(universityDummies.get(0), "TEST"),
                new MajorDto(universityDummies.get(1), "A68"),
                new MajorDto(universityDummies.get(1), "A69"),
                new MajorDto(universityDummies.get(1), "A70")
        );

        List<MajorDto> HYU_CSE_BASE_MAJORS = List.of(
                majorDummies.get(0),
                majorDummies.get(1)
        );

        List<MajorDto> HYU_STU_BASE_MAJORS = List.of(
                majorDummies.get(0),
                majorDummies.get(1),
                majorDummies.get(2),
                majorDummies.get(4)
        );

        List<MajorDto> CKU_MED_BASE_MAJORS = List.of(
                majorDummies.get(5)
        );

        List<MajorDto> CKU_STU_BASE_MAJORS = List.of(
                majorDummies.get(3),
                majorDummies.get(5),
                majorDummies.get(6),
                majorDummies.get(7)
        );

        departmentDummies = List.of(
                new DepartmentDto(universityDummies.get(0), "CSE", "컴퓨터소프트웨어학부", HYU_CSE_BASE_MAJORS),
                new DepartmentDto(universityDummies.get(0), "STU", "총학생회", HYU_STU_BASE_MAJORS),
                new DepartmentDto(universityDummies.get(1), "MED", "의과대학", CKU_MED_BASE_MAJORS),
                new DepartmentDto(universityDummies.get(1), "STU", "총학생회", CKU_STU_BASE_MAJORS)
        );

        userDummies = List.of(
                UserDto.init(universityDummies.get(0), "20180008886", "이석환"),
                UserDto.init(universityDummies.get(0), "20180008887", "이석현"),
                UserDto.init(universityDummies.get(0), "20190008887", "강백호"),
                UserDto.init(universityDummies.get(1), "20180008886", "이석환"),
                UserDto.init(universityDummies.get(1), "20180008887", "이석환")
        );

        authorityDummies = List.of(
                new AuthorityDto(userDummies.get(0), departmentDummies.get(0), AuthorityDto.Permission.MASTER),
                new AuthorityDto(userDummies.get(0), departmentDummies.get(1), AuthorityDto.Permission.USER),
                new AuthorityDto(userDummies.get(2), departmentDummies.get(0), AuthorityDto.Permission.BANNED)
        );
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