package com.belieme.apiserver.util;

import com.belieme.apiserver.domain.dto.AuthorityDto;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
  public final UserDto DEV_3_USER;
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

    DEV_UNIV = new UniversityDto(UUID.fromString("34453032-3531-3342-4643-444434464445"), "DEV",
        "DEV");
    HYU_UNIV = new UniversityDto(UUID.fromString("37364342-4345-3342-4143-344334433445"), "한양대학교",
        "https://api.hanyang.ac.kr/login");
    CKU_UNIV = new UniversityDto(UUID.fromString("41463146-4639-4435-4244-463734353333"),
        "가톨릭관동대학교", null);
    SNU_UNIV = new UniversityDto(UUID.fromString("507e25c4-9246-4863-bff8-059589989563"), "서울대학교",
        null);

    tmpAllUnivs = List.of(DEV_UNIV, HYU_UNIV, CKU_UNIV, SNU_UNIV);

    // Majors
    HYU_FH04067_MAJOR = new MajorDto(UUID.fromString("edbbfebc-63b4-46e1-9753-67451d3614f8"),
        HYU_UNIV, "FH04067");
    HYU_FH04068_MAJOR = new MajorDto(UUID.fromString("b9624786-b93f-422e-bb11-83c6c2f6cbd8"),
        HYU_UNIV, "FH04068");
    HYU_FH04069_MAJOR = new MajorDto(UUID.fromString("ebfba44e-b632-4672-98e8-e6aeb8fcca8c"),
        HYU_UNIV, "FH04069");
    CKU_A68_MAJOR = new MajorDto(UUID.fromString("422c2f41-f0fa-4256-9c59-178c68c6172c"), CKU_UNIV,
        "A68");
    CKU_A69_MAJOR = new MajorDto(UUID.fromString("272dc52d-346b-4179-91c6-e894b2ff2b63"), CKU_UNIV,
        "A69");
    CKU_A70_MAJOR = new MajorDto(UUID.fromString("40044cd4-f5e5-4a78-855c-4f4f52c92799"), CKU_UNIV,
        "A70");

    tmpAllMajors = List.of(HYU_FH04067_MAJOR, HYU_FH04068_MAJOR, HYU_FH04069_MAJOR, CKU_A68_MAJOR,
        CKU_A69_MAJOR, CKU_A70_MAJOR);

    DEV_DEPT = new DepartmentDto(UUID.fromString("37434636-4339-3345-3946-313634443633"), DEV_UNIV,
        "DEV", new ArrayList<>());
    HYU_CSE_DEPT = new DepartmentDto(UUID.fromString("c7312fe7-0626-4068-9112-51a1b8e04309"),
        HYU_UNIV, "컴퓨터소프트웨어학부", new ArrayList<>()).withBaseMajorAdd(HYU_FH04067_MAJOR)
        .withBaseMajorAdd(HYU_FH04068_MAJOR);
    HYU_ME_DEPT = new DepartmentDto(UUID.fromString("b069fa11-c416-4276-96e1-aaad27f73aeb"),
        HYU_UNIV, "기계공학부", new ArrayList<>()).withBaseMajorAdd(HYU_FH04069_MAJOR);
    HYU_ENG_DEPT = new DepartmentDto(UUID.fromString("9ebea9d2-9669-4560-990c-3628ddc29d23"),
        HYU_UNIV, "공과대학", new ArrayList<>()).withBaseMajorAdd(HYU_FH04067_MAJOR)
        .withBaseMajorAdd(HYU_FH04068_MAJOR).withBaseMajorAdd(HYU_FH04069_MAJOR);
    CKU_MED_DEPT = new DepartmentDto(UUID.fromString("fd16b1f8-20ff-47ef-9a7d-46e5d1e1c080"),
        CKU_UNIV, "의과대학", new ArrayList<>()).withBaseMajorAdd(CKU_A69_MAJOR);
    CKU_STU_DEPT = new DepartmentDto(UUID.fromString("65fd9185-48b6-407b-8bca-8177b4d64713"),
        CKU_UNIV, "총학생회", new ArrayList<>()).withBaseMajorAdd(CKU_A68_MAJOR)
        .withBaseMajorAdd(CKU_A69_MAJOR).withBaseMajorAdd(CKU_A70_MAJOR);

    tmpAllDepts = List.of(DEV_DEPT, HYU_CSE_DEPT, HYU_ME_DEPT, HYU_ENG_DEPT, CKU_MED_DEPT,
        CKU_STU_DEPT);

    // Users
    DEV_1_USER = new UserDto(UUID.fromString("38433142-3134-3231-3741-384234424539"), DEV_UNIV,
        "DEV1", "개발자1", 0, "5fc9d9a4-4a41-416f-b0b4-24db6d9c87e1", 1673155358, currentTime(),
        new ArrayList<>()).withAuthorityAdd(new AuthorityDto(DEV_DEPT, Permission.DEVELOPER));
    DEV_2_USER = new UserDto(UUID.fromString("31313446-4336-3943-3136-453534334641"), DEV_UNIV,
        "DEV2", "개발자2", 0, "eba8ee88-e9d0-4ba7-a821-769597b276c3", 1673155358, currentTime(),
        new ArrayList<>()).withAuthorityAdd(new AuthorityDto(DEV_DEPT, Permission.DEVELOPER));
    DEV_3_USER = new UserDto(UUID.fromString("31383941-3930-3237-3036-464534333241"), DEV_UNIV,
        "DEV2", "개발자3", 0, "ef09ec44-baf7-47c1-aec8-2097dd12277f", 1673155358, currentTime(),
        new ArrayList<>()).withAuthorityAdd(new AuthorityDto(DEV_DEPT, Permission.DEVELOPER));
    HYU_CSE_TESTER_1_USER = new UserDto(UUID.fromString("8ff21103-bad7-4666-a512-d73747b68cd7"),
        HYU_UNIV, "CSE-TEST1", "테스터1", 0, "6ca0bc09-349b-4e42-862a-ce9ff2e607f0", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
        new AuthorityDto(HYU_CSE_DEPT, Permission.STAFF));
    HYU_ME_TESTER_1_USER = new UserDto(UUID.fromString("0df5d108-95d0-405a-a280-aad324f21311"),
        HYU_UNIV, "ME-TEST1", "테스터1", 0, "f22121e3-7c23-4716-a123-10fc34d6cde5", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
        new AuthorityDto(HYU_ME_DEPT, Permission.USER));
    HYU_CSE_MASTER_USER = new UserDto(UUID.fromString("7adccf31-1164-49bd-9031-dd0ffb00b9ae"),
        HYU_UNIV, "2018008886", "이석환", 2018, "8231999b-3bf4-4036-afb7-34cd98810b3c", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.MASTER));
    HYU_CSE_STAFF_USER = new UserDto(UUID.fromString("87c01625-b916-466e-8d81-3be92e2527bd"),
        HYU_UNIV, "2018008887", "강백호", 2018, "ef4233a0-2bfc-4633-9cc9-38d4adbfa10e", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.STAFF));
    HYU_CSE_NORMAL_1_USER = new UserDto(UUID.fromString("058e8703-93bb-41e4-a114-e2cbf005bf54"),
        HYU_UNIV, "2018008888", "서태웅", 2018, "48ee4dc7-3279-4fe9-9159-83d98899212f", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT));
    HYU_CSE_NORMAL_2_USER = new UserDto(UUID.fromString("92e35636-d356-4cba-ac07-2ec665187b0c"),
        HYU_UNIV, "2018008889", "다빈치", 2018, "b25964ee-79f4-4b58-9115-325c2cf60205", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.USER));
    HYU_CSE_BANNED_USER = new UserDto(UUID.fromString("2831d747-8d8e-4464-8d70-f063bcfdb735"),
        HYU_UNIV, "2018008890", "박용수", 2018, "879aad94-1611-41ec-a71d-f1796100fd38", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_CSE_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.BANNED));
    HYU_DUMMY_USER_1 = new UserDto(UUID.fromString("52570b69-5c16-4bdb-bc1e-063247a93b4d"),
        HYU_UNIV, "2018007129", "김경민", 2018, "aea85218-c069-4879-b5c2-451c3ad774ba", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.MASTER));
    HYU_DUMMY_USER_2 = new UserDto(UUID.fromString("561953f1-bae3-4f34-91a9-d5fcd16f05ca"),
        HYU_UNIV, "2018007130", "손성준", 2018, "fbd703d0-1851-447f-bdb7-51edf8f3ecde", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, Permission.BANNED));
    HYU_DUMMY_USER_3 = new UserDto(UUID.fromString("35a92ffd-416c-49d5-ae64-f5af337246a5"),
        HYU_UNIV, "2018007131", "이수경", 2018, "8c4e9ebd-28ac-475f-9316-b37e97b6cf77", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT));
    HYU_DUMMY_USER_4 = new UserDto(UUID.fromString("4070d6da-6b5a-4dec-b076-18fa9bc52777"),
        HYU_UNIV, "2018007132", "황희수", 2018, "02017126-2d23-44b0-a308-c295275711d7", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(HYU_ME_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, Permission.STAFF));
    CKU_DUMMY_USER_1 = new UserDto(UUID.fromString("a010437a-9b4c-4629-8036-5a5410ced8cc"),
        CKU_UNIV, "C202201234", "박창훈", 2022, "9c22d304-c667-4d80-a692-c719bd9e2ac5", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.MASTER));
    CKU_DUMMY_USER_2 = new UserDto(UUID.fromString("cca1df09-197f-4bae-acec-7841a9da9397"),
        CKU_UNIV, "C202201235", "윤효성", 2022, "725a4da7-b354-40b2-8d78-cbb9109c7546", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(CKU_MED_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, Permission.STAFF));
    CKU_DUMMY_USER_3 = new UserDto(UUID.fromString("e74bd461-eb4a-4c97-92ab-f797b4438a88"),
        CKU_UNIV, "C202201236", "서수빈", 2022, "2af4c44f-9a67-47cd-9464-c1fc112810cd", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
        new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));
    CKU_DUMMY_USER_4 = new UserDto(UUID.fromString("2c113864-dfce-4aab-92aa-ef756c7caa55"),
        CKU_UNIV, "C202201237", "황형기", 2022, "a2fd57e8-b44d-41f2-aaab-1c530d5cca7b", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
            new AuthorityDto(CKU_MED_DEPT, Permission.DEFAULT))
        .withAuthorityAdd(new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));
    CKU_DUMMY_USER_5 = new UserDto(UUID.fromString("119b4a30-285f-4f0a-a037-a9134b7e7bb7"),
        CKU_UNIV, "C202201238", "전승용", 2022, "715a52ac-f312-4091-800c-475d9b727dbb", 1673155358,
        currentTime(), new ArrayList<>()).withAuthorityAdd(
        new AuthorityDto(CKU_STU_DEPT, Permission.DEFAULT));

    tmpAllUsers = List.of(DEV_1_USER, DEV_2_USER, DEV_3_USER, HYU_CSE_TESTER_1_USER,
        HYU_ME_TESTER_1_USER, HYU_CSE_MASTER_USER, HYU_CSE_STAFF_USER, HYU_CSE_NORMAL_1_USER,
        HYU_CSE_NORMAL_2_USER, HYU_CSE_BANNED_USER, HYU_DUMMY_USER_1, HYU_DUMMY_USER_2,
        HYU_DUMMY_USER_3, HYU_DUMMY_USER_4, CKU_DUMMY_USER_1, CKU_DUMMY_USER_2, CKU_DUMMY_USER_3,
        CKU_DUMMY_USER_4, CKU_DUMMY_USER_5);

    // Permissions
    tmpAllPermissions = Arrays.stream(Permission.values()).toList();

    // --------------------------------------------------------------------------------------
    // Stuffs init
    StuffDto SPEAKER_HYU_CSE = new StuffDto(UUID.fromString("5f35946d-a596-466c-94d3-0a09431a6309"),
        HYU_CSE_DEPT, "블루투스 스피커", "📻", new ArrayList<>());
    StuffDto UMBRELLA_HYU_CSE = new StuffDto(
        UUID.fromString("b1b566ba-ef96-4bff-9883-abb62aec7245"), HYU_CSE_DEPT, "우산", "🌂",
        new ArrayList<>());
    StuffDto PEN_HYU_CSE = new StuffDto(UUID.fromString("02143755-e80c-472b-9a23-1f9624364846"),
        HYU_CSE_DEPT, "볼펜", "🖋️", new ArrayList<>());
    StuffDto SCISSORS_HYU_CSE = new StuffDto(
        UUID.fromString("ec5de918-030f-40be-b060-3ddb415e358e"), HYU_CSE_DEPT, "가위", "✂️",
        new ArrayList<>());
    StuffDto SKATEBOARD_HYU_CSE = new StuffDto(
        UUID.fromString("a6aee430-b842-47c6-a6ef-c7410869b4a7"), HYU_CSE_DEPT, "스케이트보드", "🛹",
        new ArrayList<>());

    StuffDto UMBRELLA_HYU_ME = new StuffDto(UUID.fromString("d5405344-34f6-4b84-beaf-24f6506a4fc7"),
        HYU_ME_DEPT, "우산", "🌂", new ArrayList<>());
    StuffDto SCISSORS_HYU_ME = new StuffDto(UUID.fromString("13801bd5-b74a-43bb-b32b-323c3c59f9f2"),
        HYU_ME_DEPT, "가위", "✂️", new ArrayList<>());
    StuffDto HAMMER_HYU_ME = new StuffDto(UUID.fromString("fe54a401-27a6-4561-b749-ad392024185a"),
        HYU_ME_DEPT, "망치", "🔨", new ArrayList<>());

    StuffDto UMBRELLA_HYU_ENG = new StuffDto(
        UUID.fromString("a731bba0-f5ee-49ce-a830-9e5ae1727dad"), HYU_ENG_DEPT, "우산", "🌂",
        new ArrayList<>());
    StuffDto PEN_HYU_ENG = new StuffDto(UUID.fromString("6223fa36-7367-4099-b0d8-9bbfff04c755"),
        HYU_ENG_DEPT, "볼펜", "🖋️", new ArrayList<>());
    StuffDto SCISSORS_HYU_ENG = new StuffDto(
        UUID.fromString("cda0319e-0e85-41c9-8da7-33ccf04b4311"), HYU_ENG_DEPT, "가위", "✂️",
        new ArrayList<>());
    StuffDto CALCULATOR_HYU_ENG = new StuffDto(
        UUID.fromString("9ada08c5-8000-4ed8-a088-2486b25d8cf2"), HYU_ENG_DEPT, "계산기", "🧮️",
        new ArrayList<>());

    StuffDto PEN_CKU_MED = new StuffDto(UUID.fromString("89a9e2c1-6102-475d-b1a8-ce237c0b0223"),
        CKU_MED_DEPT, "볼펜", "🖋️", new ArrayList<>());
    StuffDto SCISSORS_CKU_MED = new StuffDto(
        UUID.fromString("0613bdfd-71a0-46ee-910d-51e9b058efee"), CKU_MED_DEPT, "가위", "✂️",
        new ArrayList<>());
    StuffDto SOCCER_BALL_CKU_MED = new StuffDto(
        UUID.fromString("eebb3378-3d04-4ba9-a1cc-297d2d0ab156"), CKU_MED_DEPT, "축구공", "⚽️️",
        new ArrayList<>());

    tmpAllStuffs = new ArrayList<>(
        List.of(SPEAKER_HYU_CSE, UMBRELLA_HYU_CSE, PEN_HYU_CSE, SCISSORS_HYU_CSE,
            SKATEBOARD_HYU_CSE, UMBRELLA_HYU_ME, SCISSORS_HYU_ME, HAMMER_HYU_ME, UMBRELLA_HYU_ENG,
            PEN_HYU_ENG, SCISSORS_HYU_ENG, CALCULATOR_HYU_ENG, PEN_CKU_MED, SCISSORS_CKU_MED,
            SOCCER_BALL_CKU_MED));

    // --------------------------------------------------------------------------------------
    // Items Init
    ItemDto SPEAKER_1_HYU_CSE = new ItemDto(UUID.fromString("91fe5765-d8da-4590-bb5f-0cc9325a0f99"),
        SPEAKER_HYU_CSE, 1, null);
    ItemDto SPEAKER_2_HYU_CSE = new ItemDto(UUID.fromString("781007d9-ac1f-440a-a2a8-faffcd366b9a"),
        SPEAKER_HYU_CSE, 2, null);
    ItemDto UMBRELLA_1_HYU_CSE = new ItemDto(
        UUID.fromString("979acb01-ea6f-47c7-a32f-9faaa5bc6fd2"), UMBRELLA_HYU_CSE, 1, null);
    ItemDto UMBRELLA_2_HYU_CSE = new ItemDto(
        UUID.fromString("e9bcb3df-40a1-4132-b6d3-57814fb42b9b"), UMBRELLA_HYU_CSE, 2, null);
    ItemDto UMBRELLA_3_HYU_CSE = new ItemDto(
        UUID.fromString("dee510ec-87b2-483c-bdbb-26798464f1f7"), UMBRELLA_HYU_CSE, 3, null);
    ItemDto UMBRELLA_4_HYU_CSE = new ItemDto(
        UUID.fromString("8977e997-e60a-4321-9616-0da5d2e92169"), UMBRELLA_HYU_CSE, 4, null);
    ItemDto PEN_1_HYU_CSE = new ItemDto(UUID.fromString("daf59579-6029-4450-a635-f5fb8a2ff7f9"),
        PEN_HYU_CSE, 1, null);
    ItemDto PEN_2_HYU_CSE = new ItemDto(UUID.fromString("573c1eef-5267-4b1e-9f18-c5539425ad52"),
        PEN_HYU_CSE, 2, null);
    ItemDto PEN_3_HYU_CSE = new ItemDto(UUID.fromString("13b0d597-f45c-4c29-90eb-583b5ae81f45"),
        PEN_HYU_CSE, 3, null);
    ItemDto PEN_4_HYU_CSE = new ItemDto(UUID.fromString("d0c3732d-6c62-4398-b43d-e59b421314ce"),
        PEN_HYU_CSE, 4, null);
    ItemDto PEN_5_HYU_CSE = new ItemDto(UUID.fromString("ee7c67ce-a9ce-475f-83c8-040423c36586"),
        PEN_HYU_CSE, 5, null);
    ItemDto PEN_6_HYU_CSE = new ItemDto(UUID.fromString("b1c9e3c9-dc2f-4ded-a7ff-4923d34f55fd"),
        PEN_HYU_CSE, 6, null);
    ItemDto SCISSORS_1_HYU_CSE = new ItemDto(
        UUID.fromString("a97821c2-29c7-4434-992b-5ceb9f6d7ca0"), SCISSORS_HYU_CSE, 1, null);
    ItemDto SCISSORS_2_HYU_CSE = new ItemDto(
        UUID.fromString("57a88c8d-446f-4d85-8cad-7bb9df81f5e7"), SCISSORS_HYU_CSE, 2, null);
    ItemDto SCISSORS_3_HYU_CSE = new ItemDto(
        UUID.fromString("59b46b31-9e35-44f6-b9a6-4a8970481438"), SCISSORS_HYU_CSE, 3, null);
    ItemDto SKATEBOARD_1_HYU_CSE = new ItemDto(
        UUID.fromString("fde3b0bf-f1b4-4529-86df-844af597244a"), SKATEBOARD_HYU_CSE, 1, null);

    tmpAllItems = new ArrayList<>(
        List.of(SPEAKER_1_HYU_CSE, SPEAKER_2_HYU_CSE, UMBRELLA_1_HYU_CSE, UMBRELLA_2_HYU_CSE,
            UMBRELLA_3_HYU_CSE, UMBRELLA_4_HYU_CSE, PEN_1_HYU_CSE, PEN_2_HYU_CSE, PEN_3_HYU_CSE,
            PEN_4_HYU_CSE, PEN_5_HYU_CSE, PEN_6_HYU_CSE, SCISSORS_1_HYU_CSE, SCISSORS_2_HYU_CSE,
            SCISSORS_3_HYU_CSE, SKATEBOARD_1_HYU_CSE));

    // --------------------------------------------------------------------------------------
    // Histories Init
    HistoryDto SPEAKER_1_1_EXPIRED_HYU_CSE = new HistoryDto(
        UUID.fromString("f820abeb-eaa3-4116-9850-d2d6867da7d8"), SPEAKER_1_HYU_CSE, 1,
        HYU_CSE_NORMAL_1_USER, null, null, null, null, 1673155356, 0, 0, 0, 0);

    HistoryDto SPEAKER_1_2_USING_HYU_CSE = new HistoryDto(
        UUID.fromString("d55195de-aa60-4dac-8bee-f04ddb713fa6"), SPEAKER_1_HYU_CSE, 2,
        HYU_CSE_NORMAL_1_USER, HYU_CSE_MASTER_USER, null, null, null, 1673155193, 1673155275, 0, 0,
        0);

    HistoryDto SPEAKER_2_1_LOST_HYU_CSE = new HistoryDto(
        UUID.fromString("dcf372d1-1df1-4e04-b2c5-68cdcf314bd8"), SPEAKER_2_HYU_CSE, 1,
        HYU_CSE_NORMAL_2_USER, HYU_CSE_STAFF_USER, null, HYU_CSE_MASTER_USER, null, 1673195193,
        1673195275, 0, 1673209275, 0);

    HistoryDto UMBRELLA_1_1_REQUESTED_HYU_CSE = new HistoryDto(
        UUID.fromString("a9b617f9-9a78-4691-b024-b79fd0b0d884"), UMBRELLA_1_HYU_CSE, 1,
        HYU_CSE_NORMAL_2_USER, null, null, null, null, System.currentTimeMillis() / 1000, 0, 0, 0,
        0);

    HistoryDto PEN_3_1_USING_HYU_CSE = new HistoryDto(
        UUID.fromString("e2e47347-8fed-4d62-b18b-dc8765add9ce"), PEN_3_HYU_CSE, 1,
        HYU_CSE_NORMAL_2_USER, HYU_CSE_STAFF_USER, null, null, null, 1673195193, 1673195275, 0, 0,
        0);

    tmpAllHistories = new ArrayList<>(
        List.of(SPEAKER_1_1_EXPIRED_HYU_CSE, SPEAKER_1_2_USING_HYU_CSE, SPEAKER_2_1_LOST_HYU_CSE,
            UMBRELLA_1_1_REQUESTED_HYU_CSE, PEN_3_1_USING_HYU_CSE));

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

  private long currentTime() {
    return System.currentTimeMillis() / 1000;
  }

  private long timeLongAgo() {
    return System.currentTimeMillis() / 1000 - 683210611;
  }
}
