package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "department", uniqueConstraints = {
        @UniqueConstraint(
                name = "department_index",
                columnNames = {"university_id", "name"}
        )
})
@NoArgsConstructor
@Getter
public class DepartmentEntity extends DataEntity<UUID> {
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
    private UniversityEntity university;

    @OneToMany(mappedBy = "department")
    private List<MajorDepartmentJoinEntity> baseMajorJoin;

    public DepartmentEntity(UUID id, UniversityEntity university, String name) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.name = name;
        this.baseMajorJoin = new ArrayList<>();
    }

    private DepartmentEntity(UUID id, UniversityEntity university, String name, List<MajorDepartmentJoinEntity> baseMajorJoin) {
        this.id = id;
        this.university = university;
        this.universityId = university.getId();
        this.name = name;
        this.baseMajorJoin = new ArrayList<>(baseMajorJoin);
    }

    public List<MajorDepartmentJoinEntity> getBaseMajorJoin() {
        return new ArrayList<>(baseMajorJoin);
    }

    public DepartmentEntity withUniversity(UniversityEntity university) {
        return new DepartmentEntity(id, university, name, baseMajorJoin);
    }

    public DepartmentEntity withName(String name) {
        return new DepartmentEntity(id, university, name, baseMajorJoin);
    }

    public DepartmentEntity withBaseMajor(List<MajorDepartmentJoinEntity> baseMajors) {
        return new DepartmentEntity(id, university, name, new ArrayList<>(baseMajorJoin));
    }

    public DepartmentEntity withBaseMajorAdd(MajorDepartmentJoinEntity baseMajor) {
        List<MajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.add(baseMajor);
        return new DepartmentEntity(id, university, name, newBaseMajors);
    }

    public DepartmentEntity withBaseMajorRemove(int index) {
        List<MajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.remove(index);
        return new DepartmentEntity(id, university, name, newBaseMajors);
    }

    public DepartmentEntity withBaseMajorClear() {
        List<MajorDepartmentJoinEntity> newBaseMajors = new ArrayList<>(baseMajorJoin);
        newBaseMajors.clear();
        return new DepartmentEntity(id, university, name, newBaseMajors);
    }

    public DepartmentDto toDepartmentDto() {
        List<MajorDto> baseMajorDtoList = new ArrayList<>();
        for (MajorDepartmentJoinEntity major : baseMajorJoin) {
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
