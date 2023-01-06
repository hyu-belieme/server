package com.example.beliemeserver.data.entity;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "authority", uniqueConstraints = {
        @UniqueConstraint(
                name = "authority_index",
                columnNames = {"user_id", "department_id"}
        )
})
@NoArgsConstructor
@ToString
@Getter
public class AuthorityEntity implements DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "department_id")
    private int department_id;


    @NonNull
    @Column(name = "permission", length = 20)
    private String permission;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id", insertable = false, updatable = false)
    private DepartmentEntity department;

    public AuthorityEntity(UserEntity user, DepartmentEntity department, String permission) {
        this.user = user;
        this.userId = user.getId();
        this.department = department;
        this.department_id = department.getId();
        this.permission = permission; // TODO : validate Permission String
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
        this.department_id = department.getId();
    }

    public void setUser(UserEntity user) {
        this.user = user;
        this.userId = user.getId();
    }

    public void setPermission(String permission) {
        this.permission = permission; // TODO : validate Permission String
    }

    public AuthorityDto toAuthorityDto() throws FormatDoesNotMatchException {
        return new AuthorityDto(
                user.toUserDto(),
                department.toDepartmentDto(),
                AuthorityDto.Permission.create(permission)
        );
    }

    public AuthorityDto toAuthorityDtoNestedToUser() throws FormatDoesNotMatchException {
        return new AuthorityDto(
                null,
                department.toDepartmentDto(),
                AuthorityDto.Permission.create(permission)
        );
    }
}
