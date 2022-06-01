package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.*;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "stuff",
        uniqueConstraints={
        @UniqueConstraint(
                name = "stuff_name",
                columnNames={"name"}
        )
})
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@IdClass(StuffId.class)
public class StuffEntity {
    private static final AtomicInteger counter = new AtomicInteger(1);
    public static int getNextId() {
        return counter.getAndIncrement();
    }

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "emoji")
    private String emoji;

    @Column(name = "next_item_num")
    private int nextItemNum;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stuff")
    private List<ItemEntity> items;

    private StuffEntity(int id, String name, String emoji, int nextItemNum, List<ItemEntity> items) {
        this.items = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.nextItemNum = nextItemNum;

        if(items != null) {
            this.items = items;
        }
    }

    public StuffEntity() {
        items = new ArrayList<>();
    }

    public int getAndIncrementNextItemNum() {
        return nextItemNum++;
    }

    public StuffDto toStuffDto() throws FormatDoesNotMatchException {
        List<ItemDto> itemDtoList = new ArrayList<>();
        Iterator<ItemEntity> iterator = items.iterator();
        while (iterator.hasNext()) {
            itemDtoList.add(iterator.next().toItemDtoNestedToStuff());
        }

        return StuffDto.builder()
                .name(name)
                .emoji(emoji)
                .items(itemDtoList)
                .build();
    }
}
