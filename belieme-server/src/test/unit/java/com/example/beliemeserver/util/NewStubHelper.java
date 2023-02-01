package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;

import java.util.ArrayList;
import java.util.List;

public class NewStubHelper {
    public final List<UniversityDto> ALL_UNIVS;
    public final List<MajorDto> ALL_MAJORS;
    public final List<DepartmentDto> ALL_DEPTS;
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
    public final UserDto DEV_USER;
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

    public NewStubHelper() {
        DEV_UNIV = new UniversityDto("DEV", "DEV_UNIV", null);
        HYU_UNIV = new UniversityDto("HYU", "한양대학교", "https://api.hanyang.ac.kr/oauth/authorize/");
        CKU_UNIV = new UniversityDto("CKU", "가톨릭관동대학교", null);
        SNU_UNIV = new UniversityDto("SNU", "서울대학교", null);

        ALL_UNIVS = List.of(
                DEV_UNIV, HYU_UNIV, CKU_UNIV, SNU_UNIV
        );

        // Majors
        HYU_FH04067_MAJOR = new MajorDto(HYU_UNIV, "FH04067");
        HYU_FH04068_MAJOR = new MajorDto(HYU_UNIV, "FH04068");
        HYU_FH04069_MAJOR = new MajorDto(HYU_UNIV, "FH04069");
        CKU_A68_MAJOR = new MajorDto(CKU_UNIV, "A68");
        CKU_A69_MAJOR = new MajorDto(CKU_UNIV, "A69");
        CKU_A70_MAJOR = new MajorDto(CKU_UNIV, "A70");

        ALL_MAJORS = List.of(
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

        ALL_DEPTS = List.of(
                DEV_DEPT, HYU_CSE_DEPT, HYU_ME_DEPT, HYU_ENG_DEPT,
                CKU_MED_DEPT, CKU_STU_DEPT
        );

        // Users
        DEV_USER = UserDto.init(DEV_UNIV, "DEV", "개발자")
                .withAuthorityAdd(new AuthorityDto(DEV_DEPT, AuthorityDto.Permission.DEVELOPER));
        HYU_CSE_MASTER_USER = UserDto.init(HYU_UNIV, "2018008886", "이석환")
                .withMajorAdd(HYU_FH04067_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.MASTER));
        HYU_CSE_STAFF_USER = UserDto.init(HYU_UNIV, "2018008887", "강백호")
                .withMajorAdd(HYU_FH04067_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.STAFF));
        HYU_CSE_NORMAL_1_USER = UserDto.init(HYU_UNIV, "2018008888", "서태웅")
                .withMajorAdd(HYU_FH04068_MAJOR);
        HYU_CSE_NORMAL_2_USER = UserDto.init(HYU_UNIV, "2018008889", "다빈치")
                .withMajorAdd(HYU_FH04069_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.USER));
        HYU_CSE_BANNED_USER = UserDto.init(HYU_UNIV, "2018008890", "박용수")
                .withMajorAdd(HYU_FH04067_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.BANNED));
        HYU_DUMMY_USER_1 = UserDto.init(HYU_UNIV, "2018007129", "김경민")
                .withMajorAdd(HYU_FH04069_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_ENG_DEPT, AuthorityDto.Permission.MASTER));
        HYU_DUMMY_USER_2 = UserDto.init(HYU_UNIV, "2018007130", "손성준")
                .withMajorAdd(HYU_FH04069_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_CSE_DEPT, AuthorityDto.Permission.BANNED));
        HYU_DUMMY_USER_3 = UserDto.init(HYU_UNIV, "2018007131", "이수경")
                .withMajorAdd(HYU_FH04069_MAJOR);
        HYU_DUMMY_USER_4 = UserDto.init(HYU_UNIV, "2018007132", "황희수")
                .withMajorAdd(HYU_FH04069_MAJOR)
                .withAuthorityAdd(new AuthorityDto(HYU_ME_DEPT, AuthorityDto.Permission.STAFF));
        CKU_DUMMY_USER_1 = UserDto.init(CKU_UNIV, "C202201234", "박창훈")
                .withMajorAdd(CKU_A68_MAJOR)
                .withMajorAdd(CKU_A70_MAJOR)
                .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, AuthorityDto.Permission.MASTER));
        CKU_DUMMY_USER_2 = UserDto.init(CKU_UNIV, "C202201235", "윤효성")
                .withMajorAdd(CKU_A68_MAJOR)
                .withAuthorityAdd(new AuthorityDto(CKU_MED_DEPT, AuthorityDto.Permission.STAFF));
        CKU_DUMMY_USER_3 = UserDto.init(CKU_UNIV, "C202201236", "서수빈")
                .withMajorAdd(CKU_A68_MAJOR);
        CKU_DUMMY_USER_4 = UserDto.init(CKU_UNIV, "C202201237", "황형기")
                .withMajorAdd(CKU_A69_MAJOR);
        CKU_DUMMY_USER_5 = UserDto.init(CKU_UNIV, "C202201238", "전승용")
                .withMajorAdd(CKU_A70_MAJOR);

        ALL_USERS = List.of(
                DEV_USER, HYU_CSE_MASTER_USER, HYU_CSE_STAFF_USER, HYU_CSE_NORMAL_1_USER,
                HYU_CSE_NORMAL_2_USER, HYU_CSE_BANNED_USER, HYU_DUMMY_USER_1, HYU_DUMMY_USER_2,
                HYU_DUMMY_USER_3, HYU_DUMMY_USER_4, CKU_DUMMY_USER_1, CKU_DUMMY_USER_2,
                CKU_DUMMY_USER_3, CKU_DUMMY_USER_4, CKU_DUMMY_USER_5
        );

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

        List<StuffDto> tmpAllStuffs = new ArrayList<>(List.of(
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

        List<ItemDto> tmpAllItems = new ArrayList<>(List.of(
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
                null, System.currentTimeMillis()/1000, 0,
                0, 0, 0
        );

        List<HistoryDto> tmpAllHistories = new ArrayList<>(List.of(
                SPEAKER_1_1_EXPIRED_HYU_CSE, SPEAKER_1_2_USING_HYU_CSE,
                SPEAKER_2_1_LOST_HYU_CSE, UMBRELLA_1_1_REQUESTED_HYU_CSE
        ));

        setUpRelations(tmpAllStuffs, tmpAllItems, tmpAllHistories);

        ALL_STUFFS = List.copyOf(tmpAllStuffs);
        ALL_ITEMS = List.copyOf(tmpAllItems);
        ALL_HISTORIES = List.copyOf(tmpAllHistories);
    }

    public UniversityDto getUnivByIdx(String univCode) {
        for(UniversityDto univ : ALL_UNIVS) {
            if(univ.matchUniqueKey(univCode)) return univ;
        }
        return null;
    }

    public DepartmentDto getDeptByIdx(String univCode, String deptCode) {
        for(DepartmentDto dept : ALL_DEPTS) {
            if(dept.matchUniqueKey(univCode, deptCode)) return dept;
        }
        return null;
    }

    public UserDto getUserByDeptAndAuth(String univCode, String deptCode, AuthorityDto.Permission permission) {
        DepartmentDto dept = getDeptByIdx(univCode, deptCode);
        for(UserDto user : ALL_USERS) {
            if(user.getMaxPermission(dept) == permission) return user;
        }
        return null;
    }

    public UserDto getUserByDeptAndAuthWithExclude(String univCode, String deptCode, AuthorityDto.Permission permission, UserDto exclude) {
        DepartmentDto dept = getDeptByIdx(univCode, deptCode);
        for(UserDto user : ALL_USERS) {
            if(user.getMaxPermission(dept) == permission
                    && !user.matchUniqueKey(exclude)) return user;
        }
        return null;
    }

    public StuffDto getStuffByIndex(String univCode, String deptCode, String stuffName) {
        for(StuffDto stuff : ALL_STUFFS) {
            if(stuff.matchUniqueKey(univCode, deptCode, stuffName)) return stuff;
        }
        return null;
    }

    public StuffDto getNthAnotherStuffWithSameDepartment(StuffDto target, int nth) {
        for(StuffDto stuff : ALL_STUFFS) {
            if(stuff.department().matchUniqueKey(target.department())
                    && !stuff.name().equals(target.name())
                    && --nth == 0) {
                return stuff;
            }
        }
        return null;
    }

    public ItemDto getItemByIndex(String univCode, String deptCode, String stuffName, int itemNum) {
        for(ItemDto item : ALL_ITEMS) {
            if(item.matchUniqueKey(univCode, deptCode, stuffName, itemNum)) return item;
        }
        return null;
    }

    public ItemDto getNthUsableItem(String univCode, String deptCode, String stuffName, int nth) {
        for(ItemDto item : ALL_ITEMS) {
            if(item.stuff().matchUniqueKey(univCode, deptCode, stuffName)
                    && item.status() == ItemDto.ItemStatus.USABLE &&--nth == 0) {
                return item;
            }
        }
        return null;
    }

    public ItemDto getNthUnusableItem(String univCode, String deptCode, String stuffName, int nth) {
        for(ItemDto item : ALL_ITEMS) {
            if(item.stuff().matchUniqueKey(univCode, deptCode, stuffName)
                    && item.status() == ItemDto.ItemStatus.UNUSABLE && --nth == 0) {
                return item;
            }
        }
        return null;
    }

    public ItemDto getNthInactiveItem(String univCode, String deptCode, String stuffName, int nth) {
        for(ItemDto item : ALL_ITEMS) {
            if(item.stuff().matchUniqueKey(univCode, deptCode, stuffName)
                    && item.status() == ItemDto.ItemStatus.INACTIVE && --nth == 0) {
                return item;
            }
        }
        return null;
    }

    public ItemDto getUsableItem(DepartmentDto dept) {
        for (ItemDto item : ALL_ITEMS) {
            if(item.stuff().department().matchUniqueKey(dept)
                    && item.status() == ItemDto.ItemStatus.USABLE) {
                return item;
            }
        }
        return null;
    }

    public ItemDto getReservedItem(DepartmentDto dept) {
        for (ItemDto item : ALL_ITEMS) {
            if(item.stuff().department().matchUniqueKey(dept)
                    && item.lastHistory().status() == HistoryDto.HistoryStatus.REQUESTED) {
                return item;
            }
        }
        return null;
    }

    public ItemDto getAnotherItemWithSameStuff(ItemDto item) {
        for(ItemDto piece : ALL_ITEMS) {
            if(piece.stuff().matchUniqueKey(item.stuff())
                    && piece.num() != item.num()) {
                return piece;
            }
        }
        return null;
    }

    public HistoryDto getHistoryByIndex(String univCode, String deptCode, String stuffName, int itemNum, int historyNum) {
        for(HistoryDto history : ALL_HISTORIES) {
            if(history.matchUniqueKey(univCode, deptCode, stuffName, itemNum, historyNum)) return history;
        }
        return null;
    }

    private void setUpRelations(List<StuffDto> stuffs, List<ItemDto> items, List<HistoryDto> histories) {
        setLastHistoryOfItem(items, histories);
        setItemsOfStuff(stuffs, items);
        setStuffOfItem(stuffs, items);
        setItemOfHistory(items, histories);
    }

    private void setLastHistoryOfItem(List<ItemDto> items, List<HistoryDto> histories) {
        for(HistoryDto history : histories) {
            HistoryDto newLastHistory = history.withItem(ItemDto.nestedEndpoint);
            for(int i = 0; i < items.size(); i++) {
                ItemDto item = items.get(i);
                if(matchUniqueKey(item, history.item())) {
                    items.set(i, item.withLastHistory(newLastHistory));
                }
            }
        }
    }

    private void setItemsOfStuff(List<StuffDto> stuffs, List<ItemDto> items) {
        for(ItemDto item : items) {
            for(int i = 0; i < stuffs.size(); i++) {
                StuffDto stuff = stuffs.get(i);
                if(matchUniqueKey(stuff, item.stuff())) {
                    stuffs.set(i, stuff.withItemAdd(item));
                }
            }
        }
    }

    private void setStuffOfItem(List<StuffDto> stuffs, List<ItemDto> items) {
        for(StuffDto stuff : stuffs) {
            for(int i = 0; i < items.size(); i++) {
                ItemDto item = items.get(i);
                if(matchUniqueKey(stuff, item.stuff())) {
                    items.set(i, item.withStuff(stuff));
                }
            }
        }
    }

    private void setItemOfHistory(List<ItemDto> items, List<HistoryDto> histories) {
        for(ItemDto item : items) {
            for(int i = 0; i < histories.size(); i++) {
                HistoryDto history = histories.get(i);
                if(matchUniqueKey(item, history.item())) {
                    histories.set(i, history.withItem(item));
                }
            }
        }
    }

    private boolean matchUniqueKey(StuffDto stuff, StuffDto oth) {
        String universityCode = oth.department().university().code();
        String departmentCode = oth.department().code();
        String stuffName = oth.name();
        return universityCode.equals(stuff.department().university().code())
                && departmentCode.equals(stuff.department().code())
                && stuffName.equals(stuff.name());
    }

    private boolean matchUniqueKey(ItemDto item, ItemDto oth) {
        String universityCode = item.stuff().department().university().code();
        String departmentCode = item.stuff().department().code();
        String stuffName = item.stuff().name();
        int itemNum = item.num();

        return universityCode.equals(oth.stuff().department().university().code())
                && departmentCode.equals(oth.stuff().department().code())
                && stuffName.equals(oth.stuff().name())
                && itemNum == oth.num();
    }
}
