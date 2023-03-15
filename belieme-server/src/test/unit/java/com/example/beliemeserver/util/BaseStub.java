package com.example.beliemeserver.util;

import com.example.beliemeserver.domain.dto.*;
import com.example.beliemeserver.domain.dto.enumeration.Permission;

import java.util.List;

public abstract class BaseStub {
    protected List<UniversityDto> ALL_UNIVS;
    protected List<MajorDto> ALL_MAJORS;
    protected List<DepartmentDto> ALL_DEPTS;
    protected List<UserDto> ALL_USERS;
    protected List<Permission> ALL_PERMISSIONS;
    protected List<StuffDto> ALL_STUFFS;
    protected List<ItemDto> ALL_ITEMS;
    protected List<HistoryDto> ALL_HISTORIES;

    protected void setUpRelations(List<StuffDto> stuffs, List<ItemDto> items, List<HistoryDto> histories) {
        setLastHistoryOfItem(items, histories);
        setItemsOfStuff(stuffs, items);
        setStuffOfItem(stuffs, items);
        setItemOfHistory(items, histories);
    }

    private void setLastHistoryOfItem(List<ItemDto> items, List<HistoryDto> histories) {
        for (HistoryDto history : histories) {
            HistoryDto newLastHistory = history.withItem(ItemDto.nestedEndpoint);
            for (int i = 0; i < items.size(); i++) {
                ItemDto item = items.get(i);
                if (item.matchUniqueKey(history.item())) {
                    items.set(i, item.withLastHistory(newLastHistory));
                }
            }
        }
    }

    private void setItemsOfStuff(List<StuffDto> stuffs, List<ItemDto> items) {
        for (ItemDto item : items) {
            for (int i = 0; i < stuffs.size(); i++) {
                StuffDto stuff = stuffs.get(i);
                if (stuff.matchUniqueKey(item.stuff())) {
                    stuffs.set(i, stuff.withItemAdd(item));
                }
            }
        }
    }

    private void setStuffOfItem(List<StuffDto> stuffs, List<ItemDto> items) {
        for (StuffDto stuff : stuffs) {
            for (int i = 0; i < items.size(); i++) {
                ItemDto item = items.get(i);
                if (stuff.matchUniqueKey(item.stuff())) {
                    items.set(i, item.withStuff(stuff));
                }
            }
        }
    }

    private void setItemOfHistory(List<ItemDto> items, List<HistoryDto> histories) {
        for (ItemDto item : items) {
            for (int i = 0; i < histories.size(); i++) {
                HistoryDto history = histories.get(i);
                if (item.matchUniqueKey(history.item())) {
                    histories.set(i, history.withItem(item));
                }
            }
        }
    }
}
