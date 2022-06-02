package com.example.beliemeserver.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class ItemDto {
    public enum ItemStatus {
        USABLE, UNUSABLE, INACTIVE, ERROR
    }

    private StuffDto stuff;
    private int num;
    private HistoryDto lastHistory;

    public Integer getLastHistoryNum() {
        if(lastHistory == null) {
            return null;
        }
        return lastHistory.getNum();
    }

    public ItemStatus getStatus() {
        if(lastHistory == null) {
            return ItemStatus.USABLE;
        }

        switch (lastHistory.getStatus()) {
            case REQUESTED:
            case USING:
            case DELAYED:
                return ItemStatus.UNUSABLE;
            case RETURNED:
            case EXPIRED:
            case FOUND:
                return ItemStatus.USABLE;
            case LOST:
                return ItemStatus.INACTIVE;
            case ERROR:
            default:
                return ItemStatus.ERROR;
        }
    }
}
