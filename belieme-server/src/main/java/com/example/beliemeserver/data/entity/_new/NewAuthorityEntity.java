package com.example.beliemeserver.data.entity._new;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "new_authority", uniqueConstraints = {
        @UniqueConstraint(
                name = "new_authority_index",
                columnNames = {"department_id", "permission"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class NewAuthorityEntity extends NewDataEntity<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NonNull
    @Column(name = "department_id", columnDefinition = "BINARY(16)")
    private UUID departmentId;

    @NonNull
    @Column(name = "permission", length = 20)
    private String permission;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private NewDepartmentEntity department;

    public NewAuthorityEntity(@NonNull NewDepartmentEntity department, @NonNull String permission) {
        this.department = department;
        this.departmentId = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public NewAuthorityEntity setDepartment(@NonNull NewDepartmentEntity department) {
        this.department = department;
        this.departmentId = department.getId();
        return this;
    }

    public NewAuthorityEntity setPermission(@NonNull String permission) {
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
