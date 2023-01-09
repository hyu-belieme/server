package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class ItemDto {
    @NonNull
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private StuffDto stuff;

    private int num;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private HistoryDto lastHistory;

    public ItemDto(@NonNull ItemDto itemDto) {
        this.stuff = itemDto.getStuff();
        this.num = itemDto.getNum();
        this.lastHistory = itemDto.getLastHistory();
    }

    public ItemDto(@NonNull StuffDto stuff, int num, HistoryDto lastHistory) {
        setStuff(stuff);
        setNum(num);
        setLastHistory(lastHistory);
    }

    public static ItemDto init(@NonNull StuffDto stuff, int itemNum) {
        return new ItemDto(stuff, itemNum, null);
    }

    public StuffDto getStuff() {
        return new StuffDto(stuff);
    }

    public HistoryDto getLastHistory() {
        if(lastHistory == null) return null;
        return new HistoryDto(lastHistory);
    }

    public ItemDto setStuff(@NonNull StuffDto stuff) {
        this.stuff = new StuffDto(stuff);
        return this;
    }

    public ItemDto setLastHistory(HistoryDto lastHistory) {
        if(lastHistory == null) {
            this.lastHistory = null;
            return this;
        }
        this.lastHistory = new HistoryDto(lastHistory);
        return this;
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

    public enum ItemStatus {
        USABLE, UNUSABLE, INACTIVE, ERROR
    }
}