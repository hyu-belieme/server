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
                name = "new_department_index",
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
        this.university = university;
        this.universityId = university.getId();
        this.name = name;
        this.baseMajorJoin = new ArrayList<>();
    }

    private NewDepartmentEntity(UUID id, NewUniversityEntity university, String name, List<NewMajorDepartmentJoinEntity> baseMajorJoin) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.name = name;
        this.baseMajorJoin = new ArrayList<>(baseMajorJoin);
    }

    public List<NewMajorDepartmentJoinEntity> getBaseMajorJoin() {
        return new ArrayList<>(baseMajorJoin);
    }

    public NewDepartmentEntity withUniversity(NewUniversityEntity university) {
        return new NewDepartmentEntity(id, university, name, baseMajorJoin);
    }

    public NewDepartmentEntity withName(String name) {
        return new NewDepartmentEntity(id, university, name, baseMajorJoin);
    }

    public NewDepartmentEntity withBaseMajor(List<NewMajorDepartmentJoinEntity> baseMajors) {
        return new NewDepartmentEntity(id, university, name, new ArrayList<>(baseMajorJoin));
    }

    public NewDepartmentEntity withBaseMajorAdd(NewMajorDepartmentJoinEntity baseMajor) {
        List<NewMajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.add(baseMajor);
        return new NewDepartmentEntity(id, university, name, newBaseMajors);
    }

    public NewDepartmentEntity withBaseMajorRemove(int index) {
        List<NewMajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.remove(index);
        return new NewDepartmentEntity(id, university, name, newBaseMajors);
    }

    public NewDepartmentEntity withBaseMajorClear() {
        List<NewMajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.clear();
        return new NewDepartmentEntity(id, university, name, newBaseMajors);
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
