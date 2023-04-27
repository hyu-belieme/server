package com.example.beliemeserver.domain.dto;

import com.example.beliemeserver.domain.dto.enumeration.HistoryStatus;
import com.example.beliemeserver.domain.dto.enumeration.ItemStatus;
import lombok.NonNull;

public record ItemDto(
        @NonNull StuffDto stuff, int num, HistoryDto lastHistory
) {
    public static final ItemDto nestedEndpoint = new ItemDto(StuffDto.nestedEndpoint, 0, null);

    public static ItemDto init(@NonNull StuffDto stuff, int itemNum) {
        return new ItemDto(stuff, itemNum, null);
    }

    public ItemDto withStuff(@NonNull StuffDto stuff) {
        return new ItemDto(stuff, num, lastHistory);
    }

    public ItemDto withNum(int num) {
        return new ItemDto(stuff, num, lastHistory);
    }

    public ItemDto withLastHistory(HistoryDto lastHistory) {
        return new ItemDto(stuff, num, lastHistory);
    }

    public boolean matchUniqueKey(String universityCode, String departmentCode, String stuffName, int num) {
        return this.stuff().matchUniqueKey(universityCode, departmentCode, stuffName)
                && num == this.num();
    }

    public boolean matchUniqueKey(ItemDto oth) {
        if (oth == null) return false;
        return this.stuff().matchUniqueKey(oth.stuff())
                && this.num() == oth.num();
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "ItemDto{" +
                "stuff=" + stuff +
                ", num=" + num +
                ", lastHistory=" + lastHistory +
                '}';
    }

    public int nextHistoryNum() {
        if (lastHistory == null) return 1;
        return lastHistory.num() + 1;
    }

    public boolean isUsable() {
        return status() == ItemStatus.USABLE;
    }

    public boolean isUnusable() {
        return !isUsable();
    }

    public ItemStatus status() {
        if (lastHistory == null
                || lastHistory.status() == HistoryStatus.RETURNED
                || lastHistory.status() == HistoryStatus.FOUND
                || lastHistory.status() == HistoryStatus.EXPIRED
        ) {
            return ItemStatus.USABLE;
        }

        if(lastHistory.status() == HistoryStatus.REQUESTED) return ItemStatus.REQUESTED;
        if(lastHistory.status() == HistoryStatus.USING || lastHistory.status() == HistoryStatus.DELAYED) return ItemStatus.USING;
        if(lastHistory.status() == HistoryStatus.LOST) return ItemStatus.LOST;
        return ItemStatus.ERROR;
    }

}