package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.entity.id.MajorDepartmentJoinId;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "major")
@NoArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@IdClass(MajorDepartmentJoinId.class)
public class MajorDepartmentJoinEntity implements DataEntity {
    @Id
    @NonNull
    @Column(name = "major_id")
    private int majorId;

    @Id
    @NonNull
    @Column(name = "department_id")
    private int departmentId;

    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MajorEntity major;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;
}
