package com.example.beliemeserver.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "major_department_join")
@NoArgsConstructor
@Getter
@ToString
public class MajorDepartmentJoinEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @NonNull
    @Column(name = "major_id")
    private int majorId;

    @NonNull
    @Column(name = "department_id")
    private int departmentId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MajorEntity major;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    public MajorDepartmentJoinEntity(MajorEntity major, DepartmentEntity department) {
        this.major = major;
        this.majorId = major.getId();
        this.department = department;
        this.departmentId = department.getId();
    }

    @PreRemove
    private void commitToDepartmentBeforeRemove() {
        department.getBaseMajorJoin().remove(this);
    }

    @PrePersist
    private void commitOnDepartmentAfterCreate() {
        department.getBaseMajorJoin().add(this);
    }
}
