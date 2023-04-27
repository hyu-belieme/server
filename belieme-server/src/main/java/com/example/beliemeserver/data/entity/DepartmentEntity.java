package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.MajorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "department", uniqueConstraints = {
        @UniqueConstraint(
                name = "department_index",
                columnNames = {"university_id", "code"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class DepartmentEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "university_id")
    private int universityId;

    @NonNull
    @Column(name = "code")
    private String code;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UniversityEntity university;

    @OneToMany(mappedBy = "department")
    private List<MajorDepartmentJoinEntity> baseMajorJoin;

    public DepartmentEntity(UniversityEntity university, String code, String name) {
        this.code = code;
        this.name = name;
        this.university = university;
        this.universityId = university.getId();
        this.baseMajorJoin = new ArrayList<>();
    }

    public DepartmentEntity setUniversity(UniversityEntity university) {
        this.university = university;
        this.universityId = university.getId();
        return this;
    }

    public DepartmentEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public DepartmentEntity setName(String name) {
        this.name = name;
        return this;
    }

    public DepartmentDto toDepartmentDto() {
        List<MajorDto> baseMajorDtoList = new ArrayList<>();
        for (MajorDepartmentJoinEntity major : baseMajorJoin) {
            baseMajorDtoList.add(major.getMajor().toMajorDto());
        }

        return new DepartmentDto(
                university.toUniversityDto(),
                code,
                name,
                baseMajorDtoList
        );
    }
}
