package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.dto.ItemDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
public class ItemEntity implements DataEntity {
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
    private int lastHistoryId;

    @Column(name = "next_history_num")
    private int nextHistoryNum;

    @ManyToOne
    @JoinColumn(name = "stuff_id", referencedColumnName = "id", insertable = false, updatable = false)
    private StuffEntity stuff;

    @OneToOne
    @JoinColumn(name = "last_history_id", referencedColumnName = "id", insertable = false, updatable = false)
    private HistoryEntity lastHistory;

    public ItemEntity(StuffEntity stuff, int num, HistoryEntity lastHistory) {
        this.stuff = stuff;
        this.stuffId = stuff.getId();
        this.num = num;
        this.lastHistory = lastHistory;
        this.lastHistoryId = getHistoryId(lastHistory);
    }

    public void setStuff(StuffEntity stuff) {
        this.stuff = stuff;
        this.stuffId = stuff.getId();
    }

    public void setLastHistory(HistoryEntity lastHistory) {
        this.lastHistory = lastHistory;
        this.lastHistoryId = getHistoryId(lastHistory);
    }

    public int getAndIncrementNextHistoryNum() {
        return nextHistoryNum++;
    }

    public ItemDto toItemDto() throws FormatDoesNotMatchException {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                stuff.toStuffDto(),
                num,
                lastHistoryDto
        );
    }

    public ItemDto toItemDtoNestedToStuff() throws FormatDoesNotMatchException {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                null,
                num,
                lastHistoryDto
        );
    }

    private HistoryDto getLastHistoryDto() throws FormatDoesNotMatchException {
        if(lastHistory == null) {
            return null;
        }
        return lastHistory.toHistoryDtoNestedToItem();
    }

    private static int getHistoryId(HistoryEntity history) {
        if(history == null) {
            return 0;
        }
        return history.getId();
    }
}
