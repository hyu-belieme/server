package com.example.beliemeserver.util;

import com.example.beliemeserver.model.dto.*;

import java.util.List;

public abstract class BaseStub {
    protected List<UniversityDto> ALL_UNIVS;
    protected List<MajorDto> ALL_MAJORS;
    protected List<DepartmentDto> ALL_DEPTS;
    protected List<UserDto> ALL_USERS;
    protected List<AuthorityDto.Permission> ALL_PERMISSIONS;
    protected List<StuffDto> ALL_STUFFS;
    protected List<ItemDto> ALL_ITEMS;
    protected List<HistoryDto> ALL_HISTORIES;

    public UniversityDto getUnivByIdx(String univCode) {
        return ALL_UNIVS.stream().filter(
                (univ) -> univ.matchUniqueKey(univCode)
        ).findAny().orElse(null);
    }

    public DepartmentDto getDeptByIdx(String univCode, String deptCode) {
        return ALL_DEPTS.stream().filter(
                (dept) -> dept.matchUniqueKey(univCode, deptCode)
        ).findAny().orElse(null);
    }

    public StuffDto getStuffByIndex(String univCode, String deptCode, String stuffName) {
        return ALL_STUFFS.stream().filter(
                (stuff) -> stuff.matchUniqueKey(univCode, deptCode, stuffName)
        ).findAny().orElse(null);
    }

    public ItemDto getItemByIndex(String univCode, String deptCode, String stuffName, int itemNum) {
        return ALL_ITEMS.stream().filter(
                (item) -> item.matchUniqueKey(univCode, deptCode, stuffName, itemNum)
        ).findAny().orElse(null);
    }

    public HistoryDto getHistoryByIndex(String univCode, String deptCode, String stuffName, int itemNum, int historyNum) {
        return ALL_HISTORIES.stream().filter(
                (history) -> history.matchUniqueKey(univCode, deptCode, stuffName, itemNum, historyNum)
        ).findAny().orElse(null);
    }

    protected void setUpRelations(List<StuffDto> stuffs, List<ItemDto> items, List<HistoryDto> histories) {
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
                if(item.matchUniqueKey(history.item())) {
                    items.set(i, item.withLastHistory(newLastHistory));
                }
            }
        }
    }

    private void setItemsOfStuff(List<StuffDto> stuffs, List<ItemDto> items) {
        for(ItemDto item : items) {
            for(int i = 0; i < stuffs.size(); i++) {
                StuffDto stuff = stuffs.get(i);
                if(stuff.matchUniqueKey(item.stuff())) {
                    stuffs.set(i, stuff.withItemAdd(item));
                }
            }
        }
    }

    private void setStuffOfItem(List<StuffDto> stuffs, List<ItemDto> items) {
        for(StuffDto stuff : stuffs) {
            for(int i = 0; i < items.size(); i++) {
                ItemDto item = items.get(i);
                if(stuff.matchUniqueKey(item.stuff())) {
                    items.set(i, item.withStuff(stuff));
                }
            }
        }
    }

    private void setItemOfHistory(List<ItemDto> items, List<HistoryDto> histories) {
        for(ItemDto item : items) {
            for(int i = 0; i < histories.size(); i++) {
                HistoryDto history = histories.get(i);
                if(item.matchUniqueKey(history.item())) {
                    histories.set(i, history.withItem(item));
                }
            }
        }
    }
}
