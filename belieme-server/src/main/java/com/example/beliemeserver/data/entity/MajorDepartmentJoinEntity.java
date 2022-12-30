package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.MajorDepartmentJoinId;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Entity
@Table(name = "major")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@IdClass(MajorDepartmentJoinId.class)
public class MajorDepartmentJoinEntity {
    @Id
    @Column(name = "major_id")
    private int majorId;

    @Id
    @Column(name = "department_id")
    private int departmentId;

    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MajorEntity major;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;
}
