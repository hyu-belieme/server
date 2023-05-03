package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "new_stuff", uniqueConstraints = {
        @UniqueConstraint(
                name = "new_stuff_index",
                columnNames = {"department_id", "name"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class NewStuffEntity extends NewDataEntity<UUID> {
    @Id
    @NonNull
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "department_id", columnDefinition = "BINARY(16)")
    private UUID departmentId;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "thumbnail")
    private String thumbnail;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewDepartmentEntity department;

    @NonNull
    @OneToMany(mappedBy = "stuff")
    private List<NewItemEntity> items;

    public NewStuffEntity(@NonNull UUID id, @NonNull NewDepartmentEntity department, @NonNull String name, @NonNull String thumbnail) {
        this.id = id;
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>();
    }

    private NewStuffEntity(@NonNull UUID id, @NonNull NewDepartmentEntity department, @NonNull String name, @NonNull String thumbnail, @NonNull List<NewItemEntity> items) {
        this.id = id;
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>(items);
    }

    public NewStuffEntity withDepartment(@NonNull NewDepartmentEntity department) {
        return new NewStuffEntity(id, department, name, thumbnail, items);
    }

    public NewStuffEntity withName(@NonNull String name) {
        return new NewStuffEntity(id, department, name, thumbnail, items);
    }

    public NewStuffEntity withThumbnail(@NonNull String thumbnail) {
        return new NewStuffEntity(id, department, name, thumbnail, items);
    }

    public StuffDto toStuffDto() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (NewItemEntity item : items) {
            itemDtoList.add(item.toItemDtoNestedToStuff());
        }

        return new StuffDto(
                id,
                department.toDepartmentDto(),
                name,
                thumbnail,
                itemDtoList
        );
    }
}
