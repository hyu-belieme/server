package com.example.beliemeserver.domain.dto;

import com.example.beliemeserver.domain.dto.enumeration.Permission;
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
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "AuthorityDto{" +
                "department=" + department +
                ", permission=" + permission +
                '}';
    }

    public boolean matchUniqueKey(String universityCode, String departmentCode, Permission permission) {
        if (universityCode == null || departmentCode == null || permission == null) {
            return false;
        }
        return universityCode.equals(this.department().university().code())
                && departmentCode.equals(this.department().code())
                && permission.equals(this.permission());
    }

    public boolean matchUniqueKey(AuthorityDto oth) {
        if (oth == null) {
            return false;
        }
        String universityCode = oth.department().university().code();
        String departmentCode = oth.department().code();
        Permission permission = oth.permission();
        return universityCode.equals(this.department().university().code())
                && departmentCode.equals(this.department().code())
                && permission.equals(this.permission());
    }

}
