package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.*;

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
public class StuffEntity implements DataEntity {
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

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    @OneToMany(mappedBy = "stuff")
    private List<ItemEntity> items;

    public StuffEntity(DepartmentEntity department, String name, String emoji) {
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.emoji = emoji;
        this.nextItemNum = 1;
        this.items = new ArrayList<>();
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
        this.departmentId = department.getId();
    }

    public int getAndIncrementNextItemNum() {
        return nextItemNum++;
    }

    public StuffDto toStuffDto() throws FormatDoesNotMatchException {
        StuffDto output = new StuffDto(
                department.toDepartmentDto(),
                name,
                emoji,
                new ArrayList<>()
        );

        for(ItemEntity item : items) {
            output.addItem(item.toItemDtoNestedToStuff());
        }
        return output;
    }
}
