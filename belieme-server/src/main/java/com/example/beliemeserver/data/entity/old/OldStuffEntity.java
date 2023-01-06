package com.example.beliemeserver.data.entity.old;

import com.example.beliemeserver.data.entity.DataEntity;
import com.example.beliemeserver.data.entity.id.*;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.dto.old.OldStuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Table(name = "old_stuff",
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
@IdClass(OldStuffId.class)
public class OldStuffEntity implements DataEntity {
    private static final AtomicInteger counter = new AtomicInteger(1);

    public static void setCounter(int initVal) {
        counter.set(initVal);
    }

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
    private List<OldItemEntity> items;

    private OldStuffEntity(int id, String name, String emoji, int nextItemNum, List<OldItemEntity> items) {
        this.items = new ArrayList<>();
        this.id = id;
        this.name = name;
        this.emoji = emoji;
        this.nextItemNum = nextItemNum;

        if(items != null) {
            this.items = items;
        }
    }

    public OldStuffEntity() {
        items = new ArrayList<>();
    }

    public int getAndIncrementNextItemNum() {
        return nextItemNum++;
    }

    public OldStuffDto toStuffDto() throws FormatDoesNotMatchException {
        List<OldItemDto> itemDtoList = new ArrayList<>();
        Iterator<OldItemEntity> iterator = items.iterator();
        while (iterator.hasNext()) {
            itemDtoList.add(iterator.next().toItemDtoNestedToStuff());
        }

        return OldStuffDto.builder()
                .name(name)
                .emoji(emoji)
                .items(itemDtoList)
                .nextItemNum(nextItemNum)
                .build();
    }
}
