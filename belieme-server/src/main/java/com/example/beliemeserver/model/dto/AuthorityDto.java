package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import lombok.NonNull;

public record AuthorityDto(
        @NonNull DepartmentDto department, @NonNull Permission permission
) {
    public static final AuthorityDto nestedEndpoint = new AuthorityDto(DepartmentDto.nestedEndpoint, Permission.BANNED);

    public AuthorityDto withDepartment(@NonNull DepartmentDto department) {
        return new AuthorityDto(department, permission);
    }

    public AuthorityDto withPermission(@NonNull Permission permission) {
        return new AuthorityDto(department, permission);
    }

    @Override
    public String toString() {
        if(this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "AuthorityDto{" +
                "department=" + department +
                ", permission=" + permission +
                '}';
    }

    public boolean matchUniqueKey(String universityCode, String departmentCode, Permission permission) {
        if(universityCode == null || departmentCode == null || permission == null) {
            return false;
        }
        return universityCode.equals(this.department().university().code())
                && departmentCode.equals(this.department().code())
                && permission.equals(this.permission());
    }

    public boolean matchUniqueKey(AuthorityDto oth) {
        if(oth == null) {
            return false;
        }
        String universityCode = oth.department().university().code();
        String departmentCode = oth.department().code();
        Permission permission = oth.permission();
        return universityCode.equals(this.department().university().code())
                && departmentCode.equals(this.department().code())
                && permission.equals(this.permission());
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
                case DEVELOPER -> true;
                case MASTER -> other != DEVELOPER;
                case STAFF -> other != DEVELOPER && other != MASTER;
                case USER -> other == BANNED || other == USER;
                case BANNED -> other == BANNED;
            };
        }
    }
}
