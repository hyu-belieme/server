package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;

import java.util.ArrayList;

public class StubHelper {
    public static final UniversityDto devUniversity = new UniversityDto("DEV", "for_developer", null);
    public static final DepartmentDto devDepartment = new DepartmentDto(devUniversity, "DEV", "for_developer", new ArrayList<>());
    public static final AuthorityDto devAuthority = new AuthorityDto(devDepartment, AuthorityDto.Permission.DEVELOPER);
    public static final UserDto developer = UserDto.init(devUniversity, "DEV", "개발자")
            .withAuthorityAdd(devAuthority);

    public static final UniversityDto basicUniversity1 = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/");
    public static final UniversityDto basicUniversity2 = new UniversityDto("CKU", "가톨릭관동대학교", null);
    public static final UniversityDto basicUniversity3 = new UniversityDto("SNU", "서울대학교", null);

    public static final MajorDto basicMajor1 = new MajorDto(basicUniversity1, "FH04067");
    public static final MajorDto basicMajor2 = new MajorDto(basicUniversity1, "FH04068");
    public static final MajorDto basicMajor3 = new MajorDto(basicUniversity2, "A68");
    public static final MajorDto basicMajor4 = new MajorDto(basicUniversity2, "A69");
    public static final MajorDto basicMajor5 = new MajorDto(basicUniversity2, "A70");

    public static final DepartmentDto basicDepartment1 = DepartmentDto.init(basicUniversity1, "CSE", "컴퓨터소프트웨어학부")
            .withBaseMajorAdd(basicMajor1)
            .withBaseMajorAdd(basicMajor2);

    public static final DepartmentDto basicDepartment2 = DepartmentDto.init(basicUniversity2, "STU", "총학생회")
            .withBaseMajorAdd(basicMajor3)
            .withBaseMajorAdd(basicMajor4)
            .withBaseMajorAdd(basicMajor5);

    public static final UserDto masterOfDepartment1 = UserDto.init(basicUniversity1, "2018008886", "이석환")
            .withMajorAdd(basicMajor1)
            .withAuthorityAdd(new AuthorityDto(basicDepartment1, AuthorityDto.Permission.MASTER));

    public static final UserDto staffOfDepartment1 = UserDto.init(basicUniversity1, "2018008887", "이석현")
            .withMajorAdd(basicMajor1)
            .withAuthorityAdd(new AuthorityDto(basicDepartment1, AuthorityDto.Permission.STAFF));

    public static final UserDto userOfDepartment1 = UserDto.init(basicUniversity1, "2018008888", "강백호")
            .withMajorAdd(basicMajor1);

    public static final UserDto bannedUserOfDepartment1 = UserDto.init(basicUniversity1, "2018008889", "서태웅")
            .withMajorAdd(basicMajor2)
            .withAuthorityAdd(new AuthorityDto(basicDepartment1, AuthorityDto.Permission.BANNED));
}
