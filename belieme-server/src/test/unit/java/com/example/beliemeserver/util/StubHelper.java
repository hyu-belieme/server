package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;

import java.util.ArrayList;
import java.util.List;

public class StubHelper {
    // TODO 이 부분 지우기
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

    // TODO ---

    // Universities
    public static final UniversityDto DEV_UNIV = new UniversityDto("DEV", "DEV_UNIV", null);
    public static final UniversityDto HYU_UNIV = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/");
    public static final UniversityDto CKU_UNIV = new UniversityDto("CKU", "가톨릭관동대학교", null);
    public static final UniversityDto SNU_UNIV = new UniversityDto("SNU", "서울대학교", null);

    public static final List<UniversityDto> ALL_UNIVS = List.of(
            DEV_UNIV, HYU_UNIV, CKU_UNIV, SNU_UNIV
    );

    // Majors
    public static final MajorDto HYU_FH04067_MAJOR = new MajorDto(HYU_UNIV, "FH04067");
    public static final MajorDto HYU_FH04068_MAJOR = new MajorDto(HYU_UNIV, "FH04068");
    public static final MajorDto HYU_FH04069_MAJOR = new MajorDto(HYU_UNIV, "FH04069");
    public static final MajorDto CKU_A68_MAJOR = new MajorDto(CKU_UNIV, "A68");
    public static final MajorDto CKU_A69_MAJOR = new MajorDto(CKU_UNIV, "A69");
    public static final MajorDto CKU_A70_MAJOR = new MajorDto(CKU_UNIV, "A70");

    public static final List<MajorDto> ALL_MAJORS = List.of(
            HYU_FH04067_MAJOR, HYU_FH04068_MAJOR, HYU_FH04069_MAJOR, CKU_A68_MAJOR,
            CKU_A69_MAJOR, CKU_A70_MAJOR
    );

    // Departments
    public static final DepartmentDto DEV_DEPT = DepartmentDto.init(DEV_UNIV, "DEV", "DEV_DEPT");
    public static final DepartmentDto HYU_CSE_DEPT = DepartmentDto.init(HYU_UNIV, "CSE", "컴퓨터소프트웨어학부")
            .withBaseMajorAdd(HYU_FH04067_MAJOR)
            .withBaseMajorAdd(HYU_FH04068_MAJOR);
    public static final DepartmentDto HYU_ME_DEPT = DepartmentDto.init(HYU_UNIV, "ME", "기계공학부")
            .withBaseMajorAdd(HYU_FH04069_MAJOR);
    public static final DepartmentDto HYU_ENG_DEPT = DepartmentDto.init(HYU_UNIV, "ENG", "공과대학")
            .withBaseMajorAdd(HYU_FH04067_MAJOR)
            .withBaseMajorAdd(HYU_FH04068_MAJOR)
            .withBaseMajorAdd(HYU_FH04069_MAJOR);
    public static final DepartmentDto CKU_MED_DEPT = DepartmentDto.init(CKU_UNIV, "MED", "의과대학")
            .withBaseMajorAdd(CKU_A69_MAJOR);
    public static final DepartmentDto CKU_STU_DEPT = DepartmentDto.init(CKU_UNIV, "STU", "총학생회")
            .withBaseMajorAdd(CKU_A68_MAJOR)
            .withBaseMajorAdd(CKU_A69_MAJOR)
            .withBaseMajorAdd(CKU_A70_MAJOR);

    public static final List<DepartmentDto> ALL_DEPTS = List.of(
            DEV_DEPT, HYU_CSE_DEPT, HYU_ME_DEPT, HYU_ENG_DEPT,
            CKU_MED_DEPT, CKU_STU_DEPT
    );

    // Users
    public static final UserDto DEV_USER = UserDto.init(DEV_UNIV, "DEV", "개발자")
            .withAuthorityAdd(new AuthorityDto(DEV_DEPT, AuthorityDto.Permission.DEVELOPER));
    public static final UserDto HYU_CSE_MASTER_USER = UserDto.init(HYU_UNIV, "2018008886", "이석환")
            .withMajorAdd(HYU_FH04067_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.MASTER));
    public static final UserDto HYU_CSE_STAFF_USER = UserDto.init(HYU_UNIV, "2018008887", "강백호")
            .withMajorAdd(HYU_FH04067_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.STAFF));
    public static final UserDto HYU_CSE_NORMAL_1_USER = UserDto.init(HYU_UNIV, "2018008888", "서태웅")
            .withMajorAdd(HYU_FH04068_MAJOR);
    public static final UserDto HYU_CSE_NORMAL_2_USER = UserDto.init(HYU_UNIV, "2018008889", "다빈치")
            .withMajorAdd(HYU_FH04069_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.USER));
    public static final UserDto HYU_CSE_BANNED_USER = UserDto.init(HYU_UNIV, "2018008890", "박용수")
            .withMajorAdd(HYU_FH04067_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.BANNED));
    public static final UserDto HYU_DUMMY_USER_1 = UserDto.init(HYU_UNIV, "2018007129", "김경민")
            .withMajorAdd(HYU_FH04069_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, AuthorityDto.Permission.MASTER));
    public static final UserDto HYU_DUMMY_USER_2 = UserDto.init(HYU_UNIV, "2018007130", "손성준")
            .withMajorAdd(HYU_FH04069_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.BANNED));
    public static final UserDto HYU_DUMMY_USER_3 = UserDto.init(HYU_UNIV, "2018007131", "이수경")
            .withMajorAdd(HYU_FH04069_MAJOR);
    public static final UserDto HYU_DUMMY_USER_4 = UserDto.init(HYU_UNIV, "2018007132", "황희수")
            .withMajorAdd(HYU_FH04069_MAJOR)
            .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, AuthorityDto.Permission.STAFF));
    public static final UserDto CKU_DUMMY_USER_1 = UserDto.init(CKU_UNIV, "C202201234", "박창훈")
            .withMajorAdd(CKU_A68_MAJOR)
            .withMajorAdd(CKU_A70_MAJOR)
            .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, AuthorityDto.Permission.MASTER));
    public static final UserDto CKU_DUMMY_USER_2 = UserDto.init(CKU_UNIV, "C202201235", "윤효성")
            .withMajorAdd(CKU_A68_MAJOR)
            .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, AuthorityDto.Permission.STAFF));
    public static final UserDto CKU_DUMMY_USER_3 = UserDto.init(CKU_UNIV, "C202201236", "서수빈")
            .withMajorAdd(CKU_A68_MAJOR);
    public static final UserDto CKU_DUMMY_USER_4 = UserDto.init(CKU_UNIV, "C202201237", "황형기")
            .withMajorAdd(CKU_A69_MAJOR);
    public static final UserDto CKU_DUMMY_USER_5 = UserDto.init(CKU_UNIV, "C202201238", "전승용")
            .withMajorAdd(CKU_A70_MAJOR);

    public static final List<UserDto> ALL_USERS = List.of(
            DEV_USER, HYU_CSE_MASTER_USER, HYU_CSE_STAFF_USER, HYU_CSE_NORMAL_1_USER,
            HYU_CSE_NORMAL_2_USER, HYU_CSE_BANNED_USER, HYU_DUMMY_USER_1, HYU_DUMMY_USER_2,
            HYU_DUMMY_USER_3, HYU_DUMMY_USER_4, CKU_DUMMY_USER_1, CKU_DUMMY_USER_2,
            CKU_DUMMY_USER_3, CKU_DUMMY_USER_4, CKU_DUMMY_USER_5
    );
}
