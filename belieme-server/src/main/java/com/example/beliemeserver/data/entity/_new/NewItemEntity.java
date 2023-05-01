package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_item", uniqueConstraints = {
        @UniqueConstraint(
                name = "new_item_index",
                columnNames = {"stuff_id", "num"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class NewItemEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "stuff_id", columnDefinition = "BINARY(16)")
    private UUID stuffId;

    @Setter
    @Column(name = "num")
    private int num;

    @Column(name = "last_history_id", columnDefinition = "BINARY(16)")
    private UUID lastHistoryId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "stuff_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewStuffEntity stuff;

    @OneToOne
    @JoinColumn(name = "last_history_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewHistoryEntity lastHistory;

    public NewItemEntity(UUID id, @NonNull NewStuffEntity stuff, int num, NewHistoryEntity lastHistory) {
        this.id = id;
        this.stuff = stuff;
        this.stuffId = stuff.getId();
        this.num = num;
        this.lastHistory = lastHistory;
        this.lastHistoryId = getIdOrElse(lastHistory, null);
    }

    public NewItemEntity setStuff(@NonNull NewStuffEntity stuff) {
        this.stuff = stuff;
        this.stuffId = stuff.getId();
        return this;
    }

    public NewItemEntity setLastHistory(NewHistoryEntity lastHistory) {
        this.lastHistory = lastHistory;
        this.lastHistoryId = getIdOrElse(lastHistory, null);
        return this;
    }

    public ItemDto toItemDto() {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                id,
                stuff.toStuffDto(),
                num,
                lastHistoryDto
        );
    }

    public ItemDto toItemDtoNestedToStuff() {
        HistoryDto lastHistoryDto = getLastHistoryDto();
        return new ItemDto(
                id,
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
