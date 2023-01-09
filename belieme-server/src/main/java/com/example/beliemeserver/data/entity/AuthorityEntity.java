package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import lombok.*;

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
    private int department_id;

    @NonNull
    @Column(name = "permission", length = 20)
    private String permission;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    public AuthorityEntity(DepartmentEntity department, String permission) {
        this.department = department;
        this.department_id = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
        this.department_id = department.getId();
    }

    public void setPermission(String permission) {
        this.permission = permission; // TODO : validate Permission String
    }

    public AuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return new AuthorityDto(
                department.toDepartmentDto(),
                AuthorityDto.Permission.create(permission)
        );
    }
}
