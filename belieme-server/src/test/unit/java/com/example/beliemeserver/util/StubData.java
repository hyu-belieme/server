package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.dto.enumeration.Permission;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StubData extends BaseStub {
    public final List<UniversityDto> ALL_UNIVS;
    public final List<MajorDto> ALL_MAJORS;
    public final List<DepartmentDto> ALL_DEPTS;
    public final List<Permission> ALL_PERMISSIONS;
    public final List<UserDto> ALL_USERS;
    public final List<StuffDto> ALL_STUFFS;
    public final List<ItemDto> ALL_ITEMS;
    public final List<HistoryDto> ALL_HISTORIES;

    // Universities
    public final UniversityDto DEV_UNIV;
    public final UniversityDto HYU_UNIV;
    public final UniversityDto CKU_UNIV;
    public final UniversityDto SNU_UNIV;

    // Majors
    public final MajorDto HYU_FH04067_MAJOR;
    public final MajorDto HYU_FH04068_MAJOR;
    public final MajorDto HYU_FH04069_MAJOR;
    public final MajorDto CKU_A68_MAJOR;
    public final MajorDto CKU_A69_MAJOR;
    public final MajorDto CKU_A70_MAJOR;

    // Departments
    public final DepartmentDto DEV_DEPT;
    public final DepartmentDto HYU_CSE_DEPT;
    public final DepartmentDto HYU_ME_DEPT;
    public final DepartmentDto HYU_ENG_DEPT;
    public final DepartmentDto CKU_MED_DEPT;
    public final DepartmentDto CKU_STU_DEPT;

    // Users
    public final UserDto DEV_1_USER;
    public final UserDto DEV_2_USER;
    public final UserDto HYU_CSE_TESTER_1_USER;
    public final UserDto HYU_ME_TESTER_1_USER;
    public final UserDto HYU_CSE_MASTER_USER;
    public final UserDto HYU_CSE_STAFF_USER;
    public final UserDto HYU_CSE_NORMAL_1_USER;
    public final UserDto HYU_CSE_NORMAL_2_USER;
    public final UserDto HYU_CSE_BANNED_USER;

    public final UserDto HYU_DUMMY_USER_1;
    public final UserDto HYU_DUMMY_USER_2;
    public final UserDto HYU_DUMMY_USER_3;
    public final UserDto HYU_DUMMY_USER_4;
    public final UserDto CKU_DUMMY_USER_1;
    public final UserDto CKU_DUMMY_USER_2;
    public final UserDto CKU_DUMMY_USER_3;
    public final UserDto CKU_DUMMY_USER_4;
    public final UserDto CKU_DUMMY_USER_5;

    public StubData() {
        List<UniversityDto> tmpAllUnivs;
        List<MajorDto> tmpAllMajors;
        List<DepartmentDto> tmpAllDepts;
        List<UserDto> tmpAllUsers;
        List<Permission> tmpAllPermissions;
        List<StuffDto> tmpAllStuffs;
        List<ItemDto> tmpAllItems;
        List<HistoryDto> tmpAllHistories;

        DEV_UNIV = new UniversityDto("DEV", "DEV_UNIV", null);
        HYU_UNIV = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/login");
        CKU_UNIV = new UniversityDto("CKU", "가톨릭관동대학교", null);
        SNU_UNIV = new UniversityDto("SNU", "서울대학교", null);

        tmpAllUnivs = List.of(
                DEV_UNIV, HYU_UNIV, CKU_UNIV, SNU_UNIV
        );

        // Majors
        HYU_FH04067_MAJOR = new MajorDto(HYU_UNIV, "FH04067");
        HYU_FH04068_MAJOR = new MajorDto(HYU_UNIV, "FH04068");
        HYU_FH04069_MAJOR = new MajorDto(HYU_UNIV, "FH04069");
        CKU_A68_MAJOR = new MajorDto(CKU_UNIV, "A68");
        CKU_A69_MAJOR = new MajorDto(CKU_UNIV, "A69");
        CKU_A70_MAJOR = new MajorDto(CKU_UNIV, "A70");

        tmpAllMajors = List.of(
                HYU_FH04067_MAJOR, HYU_FH04068_MAJOR, HYU_FH04069_MAJOR, CKU_A68_MAJOR,
                CKU_A69_MAJOR, CKU_A70_MAJOR
        );

        // Departments
        DEV_DEPT = DepartmentDto.init(DEV_UNIV, "DEV", "DEV_DEPT");
        HYU_CSE_DEPT = DepartmentDto.init(HYU_UNIV, "CSE", "컴퓨터소프트웨어학부")
                .withBaseMajorAdd(HYU_FH04067_MAJOR)
                .withBaseMajorAdd(HYU_FH04068_MAJOR);
        HYU_ME_DEPT = DepartmentDto.init(HYU_UNIV, "ME", "기계공학부")
                .withBaseMajorAdd(HYU_FH04069_MAJOR);
        HYU_ENG_DEPT = DepartmentDto.init(HYU_UNIV, "ENG", "공과대학")
                .withBaseMajorAdd(HYU_FH04067_MAJOR)
                .withBaseMajorAdd(HYU_FH04068_MAJOR)
                .withBaseMajorAdd(HYU_FH04069_MAJOR);
        CKU_MED_DEPT = DepartmentDto.init(CKU_UNIV, "MED", "의과대학")
                .withBaseMajorAdd(CKU_A69_MAJOR);
        CKU_STU_DEPT = DepartmentDto.init(CKU_UNIV, "STU", "총학생회")
                .withBaseMajorAdd(CKU_A68_MAJOR)
                .withBaseMajorAdd(CKU_A69_MAJOR)
                .withBaseMajorAdd(CKU_A70_MAJOR);

        tmpAllDepts = List.of(
                DEV_DEPT, HYU_CSE_DEPT, HYU_ME_DEPT, HYU_ENG_DEPT,
                CKU_MED_DEPT, CKU_STU_DEPT
        );

        // Users
        DEV_1_USER = UserDto.init(DEV_UNIV, "DEV1", "개발자 1")
                .withAuthorityAdd(new AuthorityDto(DEV_DEPT, Permission.DEVELOPER));
        DEV_2_USER = UserDto.init(DEV_UNIV, "DEV2", "개발자 2")
                .withAuthorityAdd(new AuthorityDto(DEV_DEPT, Permission.DEVELOPER));
        HYU_CSE_TESTER_1_USER = UserDto.init(HYU_UNIV, "CSE-TEST1", "테스터 1")
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.STAFF));
        HYU_ME_TESTER_1_USER = UserDto.init(HYU_UNIV, "ME-TEST1", "테스터 1")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.USER));
        HYU_CSE_MASTER_USER = UserDto.init(HYU_UNIV, "2018008886", "이석환")
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.MASTER));
        HYU_CSE_STAFF_USER = UserDto.init(HYU_UNIV, "2018008887", "강백호")
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.STAFF));
        HYU_CSE_NORMAL_1_USER = UserDto.init(HYU_UNIV, "2018008888", "서태웅")
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT));
        HYU_CSE_NORMAL_2_USER = UserDto.init(HYU_UNIV, "2018008889", "다빈치")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.USER));
        HYU_CSE_BANNED_USER = UserDto.init(HYU_UNIV, "2018008890", "박용수")
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.BANNED));
        HYU_DUMMY_USER_1 = UserDto.init(HYU_UNIV, "2018007129", "김경민")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.MASTER));
        HYU_DUMMY_USER_2 = UserDto.init(HYU_UNIV, "2018007130", "손성준")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.BANNED));
        HYU_DUMMY_USER_3 = UserDto.init(HYU_UNIV, "2018007131", "이수경")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT));
        HYU_DUMMY_USER_4 = UserDto.init(HYU_UNIV, "2018007132", "황희수")
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.STAFF));
        CKU_DUMMY_USER_1 = UserDto.init(CKU_UNIV, "C202201234", "박창훈")
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.MASTER));
        CKU_DUMMY_USER_2 = UserDto.init(CKU_UNIV, "C202201235", "윤효성")
                .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, Permission.STAFF));
        CKU_DUMMY_USER_3 = UserDto.init(CKU_UNIV, "C202201236", "서수빈")
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));
        CKU_DUMMY_USER_4 = UserDto.init(CKU_UNIV, "C202201237", "황형기")
                .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, Permission.DEFAULT))
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));
        CKU_DUMMY_USER_5 = UserDto.init(CKU_UNIV, "C202201238", "전승용")
                .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));

        tmpAllUsers = List.of(
                DEV_1_USER, DEV_2_USER, HYU_CSE_TESTER_1_USER, HYU_ME_TESTER_1_USER, HYU_CSE_MASTER_USER,
                HYU_CSE_STAFF_USER, HYU_CSE_NORMAL_1_USER, HYU_CSE_NORMAL_2_USER, HYU_CSE_BANNED_USER,
                HYU_DUMMY_USER_1, HYU_DUMMY_USER_2, HYU_DUMMY_USER_3, HYU_DUMMY_USER_4, CKU_DUMMY_USER_1,
                CKU_DUMMY_USER_2, CKU_DUMMY_USER_3, CKU_DUMMY_USER_4, CKU_DUMMY_USER_5
        );

        // Permissions
        tmpAllPermissions = Arrays.stream(Permission.values()).toList();

        // --------------------------------------------------------------------------------------
        // Stuffs init
        StuffDto SPEAKER_HYU_CSE = StuffDto.init(HYU_CSE_DEPT, "블루투스 스피커", "📻");
        StuffDto UMBRELLA_HYU_CSE = StuffDto.init(HYU_CSE_DEPT, "우산", "🌂");
        StuffDto PEN_HYU_CSE = StuffDto.init(HYU_CSE_DEPT, "볼펜", "🖋️");
        StuffDto SCISSORS_HYU_CSE = StuffDto.init(HYU_CSE_DEPT, "가위", "✂️");
        StuffDto SKATEBOARD_HYU_CSE = StuffDto.init(HYU_CSE_DEPT, "스케이트보드", "🛹");

        StuffDto UMBRELLA_HYU_ME = StuffDto.init(HYU_ME_DEPT, "우산", "🌂");
        StuffDto SCISSORS_HYU_ME = StuffDto.init(HYU_ME_DEPT, "가위", "✂️");
        StuffDto HAMMER_HYU_ME = StuffDto.init(HYU_ME_DEPT, "망치", "🔨");

        StuffDto UMBRELLA_HYU_ENG = StuffDto.init(HYU_ENG_DEPT, "우산", "🌂");
        StuffDto PEN_HYU_ENG = StuffDto.init(HYU_ENG_DEPT, "볼펜", "🖋️");
        StuffDto SCISSORS_HYU_ENG = StuffDto.init(HYU_ENG_DEPT, "가위", "✂️");
        StuffDto CALCULATOR_HYU_ENG = StuffDto.init(HYU_ENG_DEPT, "계산기", "🧮️");

        StuffDto PEN_CKU_MED = StuffDto.init(CKU_MED_DEPT, "볼펜", "🖋️");
        StuffDto SCISSORS_CKU_MED = StuffDto.init(CKU_MED_DEPT, "가위", "✂️");
        StuffDto SOCCER_BALL_CKU_MED = StuffDto.init(CKU_MED_DEPT, "축구공", "⚽️️");

        tmpAllStuffs = new ArrayList<>(List.of(
                SPEAKER_HYU_CSE, UMBRELLA_HYU_CSE, PEN_HYU_CSE,
                SCISSORS_HYU_CSE, SKATEBOARD_HYU_CSE, UMBRELLA_HYU_ME,
                SCISSORS_HYU_ME, HAMMER_HYU_ME, UMBRELLA_HYU_ENG,
                PEN_HYU_ENG, SCISSORS_HYU_ENG, CALCULATOR_HYU_ENG,
                PEN_CKU_MED, SCISSORS_CKU_MED, SOCCER_BALL_CKU_MED
        ));

        // --------------------------------------------------------------------------------------
        // Items Init
        ItemDto SPEAKER_1_HYU_CSE = ItemDto.init(SPEAKER_HYU_CSE, 1);
        ItemDto SPEAKER_2_HYU_CSE = ItemDto.init(SPEAKER_HYU_CSE, 2);

        ItemDto UMBRELLA_1_HYU_CSE = ItemDto.init(UMBRELLA_HYU_CSE, 1);
        ItemDto UMBRELLA_2_HYU_CSE = ItemDto.init(UMBRELLA_HYU_CSE, 2);
        ItemDto UMBRELLA_3_HYU_CSE = ItemDto.init(UMBRELLA_HYU_CSE, 3);
        ItemDto UMBRELLA_4_HYU_CSE = ItemDto.init(UMBRELLA_HYU_CSE, 4);

        ItemDto PEN_1_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 1);
        ItemDto PEN_2_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 2);
        ItemDto PEN_3_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 3);
        ItemDto PEN_4_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 4);
        ItemDto PEN_5_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 5);
        ItemDto PEN_6_HYU_CSE = ItemDto.init(PEN_HYU_CSE, 6);

        ItemDto SCISSORS_1_HYU_CSE = ItemDto.init(SCISSORS_HYU_CSE, 1);
        ItemDto SCISSORS_2_HYU_CSE = ItemDto.init(SCISSORS_HYU_CSE, 2);
        ItemDto SCISSORS_3_HYU_CSE = ItemDto.init(SCISSORS_HYU_CSE, 3);

        ItemDto SKATEBOARD_1_HYU_CSE = ItemDto.init(SKATEBOARD_HYU_CSE, 1);

        tmpAllItems = new ArrayList<>(List.of(
                SPEAKER_1_HYU_CSE, SPEAKER_2_HYU_CSE, UMBRELLA_1_HYU_CSE,
                UMBRELLA_2_HYU_CSE, UMBRELLA_3_HYU_CSE, UMBRELLA_4_HYU_CSE,
                PEN_1_HYU_CSE, PEN_2_HYU_CSE, PEN_3_HYU_CSE, PEN_4_HYU_CSE,
                PEN_5_HYU_CSE, PEN_6_HYU_CSE, SCISSORS_1_HYU_CSE,
                SCISSORS_2_HYU_CSE, SCISSORS_3_HYU_CSE, SKATEBOARD_1_HYU_CSE
        ));

        // --------------------------------------------------------------------------------------
        // Histories Init
        HistoryDto SPEAKER_1_1_EXPIRED_HYU_CSE = new HistoryDto(
                SPEAKER_1_HYU_CSE, 1, HYU_CSE_NORMAL_1_USER,
                null, null, null,
                null, 1673155356, 0,
                0, 0, 0
        );

        HistoryDto SPEAKER_1_2_USING_HYU_CSE = new HistoryDto(
                SPEAKER_1_HYU_CSE, 2, HYU_CSE_NORMAL_1_USER,
                HYU_CSE_MASTER_USER, null, null,
                null, 1673155193, 1673155275,
                0, 0, 0
        );

        HistoryDto SPEAKER_2_1_LOST_HYU_CSE = new HistoryDto(
                SPEAKER_2_HYU_CSE, 1, HYU_CSE_NORMAL_2_USER,
                HYU_CSE_STAFF_USER, null, HYU_CSE_MASTER_USER,
                null, 1673195193, 1673195275,
                0, 1673209275, 0
        );

        HistoryDto UMBRELLA_1_1_REQUESTED_HYU_CSE = new HistoryDto(
                UMBRELLA_1_HYU_CSE, 1, HYU_CSE_NORMAL_2_USER,
                null, null, null,
                null, System.currentTimeMillis() / 1000, 0,
                0, 0, 0
        );

        HistoryDto PEN_3_1_USING_HYU_CSE = new HistoryDto(
                PEN_3_HYU_CSE, 1, HYU_CSE_NORMAL_2_USER,
                HYU_CSE_STAFF_USER, null, null,
                null, 1673195193, 1673195275,
                0, 0, 0
        );

        tmpAllHistories = new ArrayList<>(List.of(
                SPEAKER_1_1_EXPIRED_HYU_CSE, SPEAKER_1_2_USING_HYU_CSE,
                SPEAKER_2_1_LOST_HYU_CSE, UMBRELLA_1_1_REQUESTED_HYU_CSE,
                PEN_3_1_USING_HYU_CSE
        ));

        setUpRelations(tmpAllStuffs, tmpAllItems, tmpAllHistories);

        ALL_UNIVS = List.copyOf(tmpAllUnivs);
        ALL_MAJORS = List.copyOf(tmpAllMajors);
        ALL_DEPTS = List.copyOf(tmpAllDepts);
        ALL_USERS = List.copyOf(tmpAllUsers);
        ALL_PERMISSIONS = List.copyOf(tmpAllPermissions);
        ALL_STUFFS = List.copyOf(tmpAllStuffs);
        ALL_ITEMS = List.copyOf(tmpAllItems);
        ALL_HISTORIES = List.copyOf(tmpAllHistories);

        setUpSuper();
    }

    private void setUpSuper() {
        super.ALL_UNIVS = this.ALL_UNIVS;
        super.ALL_MAJORS = this.ALL_MAJORS;
        super.ALL_DEPTS = this.ALL_DEPTS;
        super.ALL_USERS = this.ALL_USERS;
        super.ALL_PERMISSIONS = this.ALL_PERMISSIONS;
        super.ALL_STUFFS = this.ALL_STUFFS;
        super.ALL_ITEMS = this.ALL_ITEMS;
        super.ALL_HISTORIES = this.ALL_HISTORIES;
    }
}
