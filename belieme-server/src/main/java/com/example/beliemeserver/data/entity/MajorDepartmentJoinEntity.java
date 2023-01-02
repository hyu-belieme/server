package com.example.beliemeserver.data.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "major_department_join")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MajorDepartmentJoinEntity implements DataEntity {
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

    public MajorDepartmentJoinEntity(int majorId, int departmentId, MajorEntity major, DepartmentEntity department) {
        this.majorId = majorId;
        this.departmentId = departmentId;
        this.major = major;
        this.department = department;
    }

    @PreRemove
    private void commitToDepartmentBeforeRemove() {
        department.getMajorDepartmentJoinEntities().remove(this);
    }

    @PrePersist
    private void commitOnDepartmentAfterCreate() {
        department.getMajorDepartmentJoinEntities().add(this);
    }
}
