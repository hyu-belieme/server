package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "new_department", uniqueConstraints = {
        @UniqueConstraint(
                name = "department_index",
                columnNames = {"university_id", "name"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class NewDepartmentEntity extends NewDataEntity<UUID> {
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NonNull
    @Column(name = "university_id", columnDefinition = "BINARY(16)")
    private UUID universityId;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewUniversityEntity university;

    @OneToMany(mappedBy = "department")
    private List<NewMajorDepartmentJoinEntity> baseMajorJoin;

    public NewDepartmentEntity(UUID id, NewUniversityEntity university, String name) {
        this.id = id;
        this.name = name;
        this.university = university;
        this.universityId = university.getId();
        this.baseMajorJoin = new ArrayList<>();
    }

    public NewDepartmentEntity setId(UUID id) {
        this.id = id;
        return this;
    }

    public NewDepartmentEntity setUniversity(NewUniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public NewDepartmentEntity setName(String name) {
        this.name = name;
        return this;
    }

    public DepartmentDto toDepartmentDto() {
        List<MajorDto> baseMajorDtoList = new ArrayList<>();
        for (NewMajorDepartmentJoinEntity major : baseMajorJoin) {
            baseMajorDtoList.add(major.getMajor().toMajorDto());
        }

        return new DepartmentDto(
                id,
                university.toUniversityDto(),
                name,
                baseMajorDtoList
        );
    }
}
