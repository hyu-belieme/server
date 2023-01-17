package com.example.beliemeserver.model.dto;

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

    @Override
    public String toString() {
        if(this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "ItemDto{" +
                "stuff=" + stuff +
                ", num=" + num +
                ", lastHistory=" + lastHistory +
                '}';
    }

    public ItemStatus getStatus() {
        if(lastHistory == null) {
            return ItemStatus.USABLE;
        }

        return switch (lastHistory.status()) {
            case REQUESTED, USING, DELAYED -> ItemStatus.UNUSABLE;
            case RETURNED, EXPIRED, FOUND -> ItemStatus.USABLE;
            case LOST -> ItemStatus.INACTIVE;
            default -> ItemStatus.ERROR;
        };
    }

    public enum ItemStatus {
        USABLE, UNUSABLE, INACTIVE, ERROR
    }
}