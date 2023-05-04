package com.example.beliemeserver.domain.dto._new;

import com.example.beliemeserver.domain.dto.enumeration.HistoryStatus;
import com.example.beliemeserver.domain.dto.enumeration.ItemStatus;
import lombok.NonNull;

import java.util.UUID;

public record ItemDto(
        @NonNull UUID id, @NonNull StuffDto stuff, int num, HistoryDto lastHistory
) {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final ItemDto nestedEndpoint = new ItemDto(NIL_UUID, StuffDto.nestedEndpoint, 0, null);

    public static ItemDto init(@NonNull StuffDto stuff, int itemNum) {
        return new ItemDto(UUID.randomUUID(), stuff, itemNum, null);
    }

    public ItemDto withStuff(@NonNull StuffDto stuff) {
        return new ItemDto(id, stuff, num, lastHistory);
    }

    public ItemDto withNum(int num) {
        return new ItemDto(id, stuff, num, lastHistory);
    }

    public ItemDto withLastHistory(HistoryDto lastHistory) {
        return new ItemDto(id, stuff, num, lastHistory);
    }

    public boolean matchId(ItemDto oth) {
        if (oth == null) {
            return false;
        }
        return this.id.equals(oth.id);
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

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }
        return "ItemDto{" +
                "id=" + id +
                ", stuff=" + stuff +
                ", num=" + num +
                ", lastHistory=" + lastHistory +
                '}';
    }
}