package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.DepartmentDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department", uniqueConstraints={
        @UniqueConstraint(
                name = "department_index",
                columnNames={"university_code", "code"}
        )
})
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class DepartmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    private int id;

    @NonNull
    @Column(name = "university_code")
    private String universityCode;

    @NonNull
    @Column(name = "code")
    private String code;

    @NonNull
    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "university_code", referencedColumnName = "code", insertable = false, updatable = false)
    @Setter(AccessLevel.NONE)
    private UniversityEntity university;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
    @Setter(AccessLevel.NONE)
    private List<MajorDepartmentJoinEntity> majorDepartmentJoinEntities;

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
