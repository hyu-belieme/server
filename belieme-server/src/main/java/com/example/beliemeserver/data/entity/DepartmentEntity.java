package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.DepartmentDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department", uniqueConstraints={
        @UniqueConstraint(
                name = "department_index",
                columnNames={"university_id", "code"}
        )
})
@NoArgsConstructor
@Getter
public class DepartmentEntity implements DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NonNull
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    private List<MajorDepartmentJoinEntity> majorDepartmentJoinEntities;

    public DepartmentEntity(int id, int universityId, String code, String name) {
        this.id = id;
        this.universityId = universityId;
        this.code = code;
        this.name = name;
    }

    public DepartmentEntity(int universityId, String code, String name) {
        this.universityId = universityId;
        this.code = code;
        this.name = name;
    }

    public DepartmentDto toDepartmentDto() {
        DepartmentDto output = new DepartmentDto(
                university.toUniversityDto(),
                code,
                name
        );

        for(MajorDepartmentJoinEntity major : majorDepartmentJoinEntities) {
            output.addBaseMajor(major.getMajor().toMajorDto());
        }

        return output;
    }
}
