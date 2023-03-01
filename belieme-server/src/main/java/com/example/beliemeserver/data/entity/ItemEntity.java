package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "item", uniqueConstraints = {
        @UniqueConstraint(
                name = "item_index",
                columnNames = {"stuff_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class ItemEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "stuff_id")
    private int stuffId;

    @Setter
    @Column(name = "num")
    private int num;

    @Column(name = "last_history_id")
    private Integer lastHistoryId;

    @Column(name = "next_history_num")
    private int nextHistoryNum;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "stuff_id", referencedColumnName = "id", insertable = false, updatable = false)
    private StuffEntity stuff;

    @OneToOne
    @JoinColumn(name = "last_history_id", referencedColumnName = "id", insertable = false, updatable = false)
    private HistoryEntity lastHistory;

    public ItemEntity(@NonNull StuffEntity stuff, int num, HistoryEntity lastHistory) {
        this.stuff = stuff;
        this.stuffId = stuff.getId();
        this.num = num;
        this.lastHistory = lastHistory;
        this.lastHistoryId = getIdOrElse(lastHistory, null);
    }

    public ItemEntity setStuff(@NonNull StuffEntity stuff) {
        this.stuff = stuff;
        this.stuffId = stuff.getId();
        return this;
    }

    public ItemEntity setLastHistory(HistoryEntity lastHistory) {
        this.lastHistory = lastHistory;
        this.lastHistoryId = getIdOrElse(lastHistory, null);
        return this;
    }

    public int getAndIncrementNextHistoryNum() {
        return nextHistoryNum++;
    }

    public ItemDto toItemDto() {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                stuff.toStuffDto(),
                num,
                lastHistoryDto
        );
    }

    public ItemDto toItemDtoNestedToStuff() {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                StuffDto.nestedEndpoint,
                num,
                lastHistoryDto
        );
    }

    private HistoryDto getLastHistoryDto() {
        if (lastHistory == null) {
            return null;
        }
        return lastHistory.toHistoryDtoNestedToItem();
    }
}
