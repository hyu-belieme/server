package com.belieme.apiserver.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "MAJOR_DEPARTMENT_JOIN_")
@NoArgsConstructor
@Getter
@ToString
public class MajorDepartmentJoinEntity extends DataEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Integer id;

    @NonNull
    @Column(name = "major_id", columnDefinition = "BINARY(16)")
    private UUID majorId;

    @NonNull
    @ToString.Exclude
    @Column(name = "department_id", columnDefinition = "BINARY(16)")
    private UUID departmentId;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MajorEntity major;

    @NonNull
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    public MajorDepartmentJoinEntity(@NonNull MajorEntity major, @NonNull DepartmentEntity department) {
        this.major = major;
        this.majorId = major.getId();
        this.department = department;
        this.departmentId = department.getId();
    }
}
