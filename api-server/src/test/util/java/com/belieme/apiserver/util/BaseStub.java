package com.belieme.apiserver.util;

import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.MajorDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
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

  protected void setUpRelations(List<StuffDto> stuffs, List<ItemDto> items,
      List<HistoryDto> histories) {
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
        if (item.matchId(history.item())) {
          items.set(i, item.withLastHistory(newLastHistory));
        }
      }
    }
  }

  private void setItemsOfStuff(List<StuffDto> stuffs, List<ItemDto> items) {
    for (ItemDto item : items) {
      for (int i = 0; i < stuffs.size(); i++) {
        StuffDto stuff = stuffs.get(i);
        if (stuff.matchId(item.stuff())) {
          stuffs.set(i, stuff.withItemAdd(item));
        }
      }
    }
  }

  private void setStuffOfItem(List<StuffDto> stuffs, List<ItemDto> items) {
    for (StuffDto stuff : stuffs) {
      for (int i = 0; i < items.size(); i++) {
        ItemDto item = items.get(i);
        if (stuff.matchId(item.stuff())) {
          items.set(i, item.withStuff(stuff));
        }
      }
    }
  }

  private void setItemOfHistory(List<ItemDto> items, List<HistoryDto> histories) {
    for (ItemDto item : items) {
      for (int i = 0; i < histories.size(); i++) {
        HistoryDto history = histories.get(i);
        if (item.matchId(history.item())) {
          histories.set(i, history.withItem(item));
        }
      }
    }
  }
}
