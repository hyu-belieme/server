package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;

import java.util.ArrayList;
import java.util.List;

public class DummyDataSet {
    public static List<UniversityDto> universityDummies;
    public static List<MajorDto> majorDummies;
    public static List<DepartmentDto> departmentDummies;
    public static List<UserDto> userDummies;
    public static List<AuthorityDto> authorityDummies;
    public static List<StuffDto> stuffDummies;
    public static List<ItemDto> itemDummies;
    public static List<HistoryDto> historyDummies;

    public static UniversityDto notFoundUniversity;
    public static DepartmentDto notFoundDepartment;
    public static MajorDto notFoundMajor;

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

        authorityDummies = new ArrayList<>();
        for(int i = 0; i < departmentDummies.size() - 1; i++) {
            for(AuthorityDto.Permission permission : AuthorityDto.Permission.values()) {
                authorityDummies.add(new AuthorityDto(departmentDummies.get(i), permission));
            }
        }

        userDummies = List.of(
                new UserDto(universityDummies.get(0), "2018008886", "이석환",
                        "TEST1", 1673155358, 1673155358,
                        List.of(majorDummies.get(0)), new ArrayList<>()),
                new UserDto(universityDummies.get(0), "2018008887", "이석현",
                        "TEST2", 1673155358, 1673155358,
                        List.of(majorDummies.get(1), majorDummies.get(4)), new ArrayList<>()),
                new UserDto(universityDummies.get(0), "2019008887", "강백호",
                        "TEST3", 1673155358, 1673155358,
                        List.of(majorDummies.get(2), majorDummies.get(4)), new ArrayList<>()),
                new UserDto(universityDummies.get(1), "2018008886", "이석환",
                        "TEST4", 1673155358, 1673155358,
                        List.of(majorDummies.get(5)), new ArrayList<>()),
                new UserDto(universityDummies.get(1), "2018008887", "이석환",
                        "TEST5", 1673155358, 1673155358,
                       List.of(majorDummies.get(7)), new ArrayList<>())
        );

        userDummies.get(0).addAuthority(authorityDummies.get(0 * 5 + 3));
        userDummies.get(0).addAuthority(authorityDummies.get(1 * 5 + 3));
        userDummies.get(0).addAuthority(authorityDummies.get(2 * 5 + 1));

        userDummies.get(1).addAuthority(authorityDummies.get(0 * 5 + 0));
        userDummies.get(3).addAuthority(authorityDummies.get(2 * 5 + 3));

        stuffDummies = List.of(
                StuffDto.init(departmentDummies.get(0), "우산", "☂"),
                StuffDto.init(departmentDummies.get(0), "블루투스스피커", "📻"),
                StuffDto.init(departmentDummies.get(0), "축구공", "⚽️"),
                StuffDto.init(departmentDummies.get(0), "농구공", "🏀"),
                StuffDto.init(departmentDummies.get(1), "축구공", "⚽️"),
                StuffDto.init(departmentDummies.get(1), "농구공", "🏀"),
                StuffDto.init(departmentDummies.get(2), "볼펜", "🖋️"),
                StuffDto.init(departmentDummies.get(2), "스케이트보드", "🛹")
        );

        itemDummies = List.of(
                ItemDto.init(stuffDummies.get(0), 1),
                ItemDto.init(stuffDummies.get(0), 2),
                ItemDto.init(stuffDummies.get(0), 3),
                ItemDto.init(stuffDummies.get(0), 4),
                ItemDto.init(stuffDummies.get(1), 1),
                ItemDto.init(stuffDummies.get(1), 2),
                ItemDto.init(stuffDummies.get(2), 1),
                ItemDto.init(stuffDummies.get(3), 1),
                ItemDto.init(stuffDummies.get(4), 1),
                ItemDto.init(stuffDummies.get(4), 2),
                ItemDto.init(stuffDummies.get(4), 3),
                ItemDto.init(stuffDummies.get(5), 1),
                ItemDto.init(stuffDummies.get(5), 2),
                ItemDto.init(stuffDummies.get(5), 3),
                ItemDto.init(stuffDummies.get(6), 1),
                ItemDto.init(stuffDummies.get(6), 2),
                ItemDto.init(stuffDummies.get(6), 3),
                ItemDto.init(stuffDummies.get(6), 4),
                ItemDto.init(stuffDummies.get(6), 5),
                ItemDto.init(stuffDummies.get(6), 6),
                ItemDto.init(stuffDummies.get(6), 7),
                ItemDto.init(stuffDummies.get(6), 8),
                ItemDto.init(stuffDummies.get(7), 1)
        );

        for(ItemDto item : itemDummies) {
            item.getStuff().addItem(item);
        }

        historyDummies = List.of(
                new HistoryDto(itemDummies.get(0), 1, userDummies.get(0), userDummies.get(0), userDummies.get(0), null, null, 1673155356, 1673155430, 1673159244, 0, 0),
                new HistoryDto(itemDummies.get(5), 1, userDummies.get(0), null, null, null, null, 1673155356, 0, 0, 0, 0),
                new HistoryDto(itemDummies.get(0), 2, userDummies.get(1), userDummies.get(0), null, null, null, 1673172221, 1673172521, 0, 0, 0),
                new HistoryDto(itemDummies.get(16), 1, userDummies.get(0), null, null, null, userDummies.get(0), 1673172221, 0, 0, 0, 1673172521)
        );

        for(HistoryDto history : historyDummies) {
            history.getItem().setLastHistory(history);
        }
        initSampleData();
    }

    private static void initSampleData() {
        notFoundUniversity = new UniversityDto("HANYANG", "한양대학교", null);
        notFoundDepartment = new DepartmentDto(DummyDataSet.universityDummies.get(0), "COMPUTER", "컴퓨터소프트웨어학부");
        notFoundMajor = new MajorDto(DummyDataSet.universityDummies.get(0), "DOESNT_EXIST");
    }
}