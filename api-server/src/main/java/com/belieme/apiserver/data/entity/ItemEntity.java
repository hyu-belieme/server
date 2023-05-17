package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ITEM_", uniqueConstraints = {
    @UniqueConstraint(name = "item_index", columnNames = {"stuff_id", "num"})})
@NoArgsConstructor
@ToString
@Getter
public class ItemEntity extends DataEntity<UUID> {

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
  private StuffEntity stuff;

  @OneToOne
  @JoinColumn(name = "last_history_id", referencedColumnName = "id", insertable = false, updatable = false)
  private HistoryEntity lastHistory;

  public ItemEntity(UUID id, @NonNull StuffEntity stuff, int num, HistoryEntity lastHistory) {
    this.id = id;
    this.stuff = stuff;
    this.stuffId = stuff.getId();
    this.num = num;
    this.lastHistory = lastHistory;
    this.lastHistoryId = getIdOrElse(lastHistory, null);
  }

  public ItemEntity withStuff(@NonNull StuffEntity stuff) {
    return new ItemEntity(id, stuff, num, lastHistory);
  }

  public ItemEntity withNum(int num) {
    return new ItemEntity(id, stuff, num, lastHistory);
  }

  public ItemEntity withLastHistory(HistoryEntity lastHistory) {
    return new ItemEntity(id, stuff, num, lastHistory);
  }

  public ItemDto toItemDto() {
    HistoryDto lastHistoryDto = getLastHistoryDto();
    return new ItemDto(id, stuff.toStuffDto(), num, lastHistoryDto);
  }

  public ItemDto toItemDtoNestedToStuff() {
    HistoryDto lastHistoryDto = getLastHistoryDto();
    return new ItemDto(id, StuffDto.nestedEndpoint, num, lastHistoryDto);
  }

  private HistoryDto getLastHistoryDto() {
    if (lastHistory == null) {
      return null;
    }
    return lastHistory.toHistoryDtoNestedToItem();
  }
}
