package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.enumeration.Permission;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import jakarta.persistence.*;
import java.util.UUID;

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
public class AuthorityEntity extends DataEntity<Integer> {
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
    private DepartmentEntity department;

    public AuthorityEntity(@NonNull DepartmentEntity department, @NonNull String permission) {
        this.department = department;
        this.departmentId = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public AuthorityEntity(int id, @NonNull DepartmentEntity department, @NonNull String permission) {
        this.id = id;
        this.department = department;
        this.departmentId = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public AuthorityDto toAuthorityDto() {
        return new AuthorityDto(
                department.toDepartmentDto(),
                Permission.valueOf(permission)
        );
    }
}
