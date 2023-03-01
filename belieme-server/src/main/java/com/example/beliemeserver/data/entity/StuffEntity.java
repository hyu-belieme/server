package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stuff", uniqueConstraints = {
        @UniqueConstraint(
                name = "stuff_index",
                columnNames = {"department_id", "name"}
        )
})
@NoArgsConstructor
@ToString
@Getter
@Accessors(chain = true)
public class StuffEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "department_id")
    private int departmentId;

    @NonNull
    @Setter
    @Column(name = "name")
    private String name;

    @NonNull
    @Setter
    @Column(name = "emoji")
    private String emoji;

    @Column(name = "next_item_num")
    private int nextItemNum;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    @NonNull
    @OneToMany(mappedBy = "stuff")
    private List<ItemEntity> items;

    public StuffEntity(@NonNull DepartmentEntity department, @NonNull String name, @NonNull String emoji) {
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.emoji = emoji;
        this.nextItemNum = 1;
        this.items = new ArrayList<>();
    }

    public StuffEntity setDepartment(@NonNull DepartmentEntity department) {
        this.department = department;
        this.departmentId = department.getId();
        return this;
    }

    public int getAndIncrementNextItemNum() {
        return nextItemNum++;
    }

    public StuffDto toStuffDto() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (ItemEntity item : items) {
            itemDtoList.add(item.toItemDtoNestedToStuff());
        }

        return new StuffDto(
                department.toDepartmentDto(),
                name,
                emoji,
                itemDtoList
        );
    }
}
