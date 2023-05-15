package com.belieme.apiserver.data.entity;

import com.belieme.apiserver.domain.dto.ItemDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "STUFF_", uniqueConstraints = {
        @UniqueConstraint(
                name = "stuff_index",
                columnNames = {"department_id", "name"}
        )
})
@NoArgsConstructor
@Getter
public class StuffEntity extends DataEntity<UUID> {
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
    private DepartmentEntity department;

    @NonNull
    @OneToMany(mappedBy = "stuff")
    private List<ItemEntity> items;

    public StuffEntity(@NonNull UUID id, @NonNull DepartmentEntity department, @NonNull String name, @NonNull String thumbnail) {
        this.id = id;
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>();
    }

    private StuffEntity(@NonNull UUID id, @NonNull DepartmentEntity department, @NonNull String name, @NonNull String thumbnail, @NonNull List<ItemEntity> items) {
        this.id = id;
        this.department = department;
        this.departmentId = department.getId();
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>(items);
    }

    public StuffEntity withDepartment(@NonNull DepartmentEntity department) {
        return new StuffEntity(id, department, name, thumbnail, items);
    }

    public StuffEntity withName(@NonNull String name) {
        return new StuffEntity(id, department, name, thumbnail, items);
    }

    public StuffEntity withThumbnail(@NonNull String thumbnail) {
        return new StuffEntity(id, department, name, thumbnail, items);
    }

    public StuffDto toStuffDto() {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (ItemEntity item : items) {
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
