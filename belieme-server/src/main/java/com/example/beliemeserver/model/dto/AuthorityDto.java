package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class AuthorityDto {
    @NonNull
    @Setter(AccessLevel.NONE)
    private DepartmentDto department;

    @NonNull
    private Permission permission;

    public AuthorityDto(@NonNull AuthorityDto authorityDto) {
        this.department = authorityDto.getDepartment();
        this.permission = authorityDto.getPermission();
    }

    public AuthorityDto(@NonNull DepartmentDto department, @NonNull Permission permission) {
        setDepartment(department);
        setPermission(permission);
    }

    public DepartmentDto getDepartment() {
        return new DepartmentDto(department);
    }

    public AuthorityDto setDepartment(@NonNull DepartmentDto department) {
        this.department = new DepartmentDto(department);
        return this;
    }

    public enum Permission {
        BANNED, USER, STAFF, MASTER, DEVELOPER;

        public static Permission create(String string) throws FormatDoesNotMatchException {
            return switch (string) {
                case "BANNED" -> BANNED;
                case "USER" -> USER;
                case "STAFF" -> STAFF;
                case "MASTER" -> MASTER;
                case "DEVELOPER" -> DEVELOPER;
                default -> throw new FormatDoesNotMatchException();
            };
        }

        public boolean hasUserPermission() {
            return switch (this) {
                case DEVELOPER, MASTER, STAFF, USER -> true;
                default -> false;
            };
        }

        public boolean hasStaffPermission() {
            return switch (this) {
                case DEVELOPER, MASTER, STAFF -> true;
                default -> false;
            };
        }

        public boolean hasMasterPermission() {
            return this == DEVELOPER || this == MASTER;
        }

        public boolean hasDeveloperPermission() {
            return this == DEVELOPER;
        }

        public boolean hasMorePermission(Permission other) {
            return switch (this) {
                case BANNED -> true;
                case USER -> other != BANNED && other != USER;
                case STAFF -> other == MASTER || other == DEVELOPER;
                case MASTER -> other == DEVELOPER;
                default -> false;
            };
        }
    }
}
