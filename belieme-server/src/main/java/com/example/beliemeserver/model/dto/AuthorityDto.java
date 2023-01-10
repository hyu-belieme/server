package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.data.exception.FormatDoesNotMatchException;
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
