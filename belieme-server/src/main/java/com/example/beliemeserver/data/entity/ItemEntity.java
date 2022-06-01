package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;
import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(ItemId.class)
public class ItemEntity {
    @Id
    @ManyToOne
    @JoinColumn(name = "stuff_id", referencedColumnName = "id")
    private StuffEntity stuff;

    @Id
    @Column(name = "num")
    private int num;

    @Column(name = "last_history_num")
    private Integer lastHistoryNum;

    @Column(name = "next_history_num")
    private int nextHistoryNum;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "stuff_id", referencedColumnName = "stuff_id", insertable = false, updatable = false),
            @JoinColumn(name = "num", referencedColumnName = "item_num", insertable = false, updatable = false),
            @JoinColumn(name = "last_history_num", referencedColumnName = "num", insertable = false, updatable = false)
    })
    private HistoryEntity lastHistory;

    public int getAndIncrementNextHistoryNum() {
        return nextHistoryNum++;
    }

    public ItemDto toItemDto() throws FormatDoesNotMatchException {
        HistoryDto lastHistoryDto = null;
        if(lastHistory != null) {
            lastHistoryDto = lastHistory.toHistoryDtoNestedToItem();
        }

        return ItemDto.builder()
                .stuff(stuff.toStuffDto())
                .num(num)
                .lastHistory(lastHistoryDto)
                .build();
    }

    public ItemDto toItemDtoNestedToStuff() throws FormatDoesNotMatchException {
        HistoryDto lastHistoryDto = null;
        if(lastHistory != null) {
            lastHistoryDto = lastHistory.toHistoryDtoNestedToItem();
        }

        return ItemDto.builder()
                .stuff(null)
                .num(num)
                .lastHistory(lastHistoryDto)
                .build();
    }
}
