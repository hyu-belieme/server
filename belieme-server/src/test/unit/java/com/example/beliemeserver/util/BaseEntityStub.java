package com.example.beliemeserver.util;

import com.example.beliemeserver.data.entity._new.*;
import com.example.beliemeserver.domain.dto._new.*;

import java.util.List;

public class BaseEntityStub {
    protected List<NewUniversityEntity> ALL_UNIVS;
    protected List<NewMajorEntity> ALL_MAJORS;
    protected List<NewDepartmentEntity> ALL_DEPTS;
    protected List<NewUserEntity> ALL_USERS;
    protected List<NewAuthorityEntity> ALL_AUTHS;
    protected List<NewStuffEntity> ALL_STUFFS;
    protected List<NewItemEntity> ALL_ITEMS;
    protected List<NewHistoryEntity> ALL_HISTORIES;

    protected void setUpRelations(List<NewItemEntity> items, List<NewHistoryEntity> histories) {
        setLastHistoryOfItems(items, histories);
    }

    private void setLastHistoryOfItems(List<NewItemEntity> items, List<NewHistoryEntity> histories) {
        for (NewHistoryEntity history : histories) {
            for (NewItemEntity item : items) {
                updateLastHistory(item, history);
            }
        }
    }

    private void updateLastHistory(NewItemEntity item, NewHistoryEntity history) {
        if (item.getId().equals(history.getItemId())) {
            if(item.getLastHistory() == null) {
                item.setLastHistory(history);
                return;
            }

            if(item.getLastHistory().getNum() < history.getNum()) {
                item.setLastHistory(history);
            }
        }
    }
}
