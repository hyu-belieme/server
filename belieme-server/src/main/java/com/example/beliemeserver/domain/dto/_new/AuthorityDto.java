package com.example.beliemeserver.domain.dto._new;

import com.example.beliemeserver.domain.dto.enumeration.Permission;
import lombok.NonNull;

public record AuthorityDto(
        @NonNull DepartmentDto department,
        @NonNull Permission permission
) {
    public static final AuthorityDto nestedEndpoint = new AuthorityDto(DepartmentDto.nestedEndpoint, Permission.BANNED);

    public AuthorityDto withDepartment(@NonNull DepartmentDto department) {
        return new AuthorityDto(department, permission);
    }

    public AuthorityDto withPermission(@NonNull Permission permission) {
        return new AuthorityDto(department, permission);
    }

    public boolean matchUniqueKey(AuthorityDto oth) {
        if (oth == null) {
            return false;
        }
        return this.department.matchId(oth.department) && this.permission.equals(oth.permission);
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "AuthorityDto{" +
                "department=" + department +
                ", permission=" + permission +
                '}';
    }
}
