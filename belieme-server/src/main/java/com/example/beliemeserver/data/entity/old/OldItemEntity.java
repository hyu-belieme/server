package com.example.beliemeserver.data.entity.old;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.id.*;
import com.example.beliemeserver.data.exception.old.OldFormatDoesNotMatchException;

import com.example.beliemeserver.model.dto.old.OldHistoryDto;
import com.example.beliemeserver.model.dto.old.OldItemDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "old_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Accessors(chain = true)
@IdClass(OldItemId.class)
public class OldItemEntity extends DataEntity {
    @Id
    @Column(name = "stuff_id")
    private int stuffId;

    @ManyToOne
    @JoinColumn(name = "stuff_id", referencedColumnName = "id", insertable = false, updatable = false)
    private OldStuffEntity stuff;

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
    private OldHistoryEntity lastHistory;

    public int getAndIncrementNextHistoryNum() {
        return nextHistoryNum++;
    }

    public OldItemDto toItemDto() throws OldFormatDoesNotMatchException {
        OldHistoryDto lastHistoryDto = null;
        if(lastHistory != null) {
            lastHistoryDto = lastHistory.toHistoryDtoNestedToItem();
        }

        return OldItemDto.builder()
                .stuff(stuff.toStuffDto())
                .num(num)
                .lastHistory(lastHistoryDto)
                .build();
    }

    public OldItemDto toItemDtoNestedToStuff() throws OldFormatDoesNotMatchException {
        OldHistoryDto lastHistoryDto = null;
        if(lastHistory != null) {
            lastHistoryDto = lastHistory.toHistoryDtoNestedToItem();
        }

        return OldItemDto.builder()
                .stuff(null)
                .num(num)
                .lastHistory(lastHistoryDto)
                .build();
    }

    @Override
    public int getId() {
        return 0;
    }
}
