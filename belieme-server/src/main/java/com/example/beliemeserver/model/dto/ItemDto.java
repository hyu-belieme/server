package com.example.beliemeserver.model.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class ItemDto {
    public enum ItemStatus {
        USABLE, UNUSABLE, INACTIVE, ERROR
    }

    private final StuffDto stuff;
    private final int num;
    private HistoryDto lastHistory;

    public static ItemDto init(StuffDto stuff) {
        return new ItemDto(stuff, 0, null);
    }

    public ItemStatus getStatus() {
        if(lastHistory == null) {
            return ItemDto.ItemStatus.USABLE;
        }

        return switch (lastHistory.getStatus()) {
            case REQUESTED, USING, DELAYED -> ItemStatus.UNUSABLE;
            case RETURNED, EXPIRED, FOUND -> ItemStatus.USABLE;
            case LOST -> ItemStatus.INACTIVE;
            default -> ItemStatus.ERROR;
        };
    }
}