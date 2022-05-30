package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;
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
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "stuff_id")
    private StuffEntity stuff;

    @Id
    @Column(name = "num")
    private int num;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "last_history_id")
    private HistoryEntity lastHistory;

}
