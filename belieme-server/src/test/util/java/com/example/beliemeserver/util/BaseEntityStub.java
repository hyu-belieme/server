package com.example.beliemeserver.util;

import com.example.beliemeserver.data.entity.*;

import java.util.List;

public class BaseEntityStub {
    protected List<UniversityEntity> ALL_UNIVS;
    protected List<MajorEntity> ALL_MAJORS;
    protected List<DepartmentEntity> ALL_DEPTS;
    protected List<UserEntity> ALL_USERS;
    protected List<AuthorityEntity> ALL_AUTHS;
    protected List<StuffEntity> ALL_STUFFS;
    protected List<ItemEntity> ALL_ITEMS;
    protected List<HistoryEntity> ALL_HISTORIES;

    protected void setUpRelations(List<ItemEntity> items, List<HistoryEntity> histories) {
        setLastHistoryOfItems(items, histories);
    }

    private void setLastHistoryOfItems(List<ItemEntity> items, List<HistoryEntity> histories) {
        for (HistoryEntity history : histories) {
            for (int i = 0; i < items.size(); i++) {
                ItemEntity item = items.get(i);
                items.set(i, updateLastHistory(item, history));
            }
        }
    }

    private ItemEntity updateLastHistory(ItemEntity item, HistoryEntity history) {
        if (item.getId().equals(history.getItemId())) {
            if(item.getLastHistory() == null) {
                return item.withLastHistory(history);
            }

            if(item.getLastHistory().getNum() < history.getNum()) {
                return item.withLastHistory(history);
            }
        }
        return item;
    }
}
