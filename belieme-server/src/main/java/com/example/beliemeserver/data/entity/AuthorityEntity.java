package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.enumeration.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "authority", uniqueConstraints = {
        @UniqueConstraint(
                name = "authority_index",
                columnNames = {"department_id", "permission"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class AuthorityEntity extends DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "department_id")
    private int departmentId;

    @NonNull
    @Column(name = "permission", length = 20)
    private String permission;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    public AuthorityEntity(DepartmentEntity department, String permission) {
        this.department = department;
        this.departmentId = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public AuthorityEntity setDepartment(DepartmentEntity department) {
        this.department = department;
        this.departmentId = department.getId();
        return this;
    }

    public AuthorityEntity setPermission(String permission) {
        this.permission = permission; // TODO : validate Permission String
        return this;
    }

    public AuthorityDto toAuthorityDto() {
        return new AuthorityDto(
                department.toDepartmentDto(),
                Permission.valueOf(permission)
        );
    }
}
