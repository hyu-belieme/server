package com.example.beliemeserver.domain.dto;

import com.example.beliemeserver.domain.dto.enumeration.Permission;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserDto(
        @NonNull UniversityDto university, @NonNull String studentId,
        @NonNull String name, @NonNull String token,
        long createdAt, long approvedAt,
        @NonNull List<AuthorityDto> authorities
) {
    public static final UserDto nestedEndpoint = new UserDto(UniversityDto.nestedEndpoint, "-", "-", "", 0, 0, new ArrayList<>());

    public UserDto(
            @NonNull UniversityDto university, @NonNull String studentId,
            @NonNull String name, @NonNull String token,
            long createdAt, long approvedAt,
            @NonNull List<AuthorityDto> authorities
    ) {
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
        this.authorities = new ArrayList<>(authorities);
    }

    public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId, @NonNull String name) {
        return new UserDto(university, studentId, name, newToken(),
                currentTime(), currentTime(), new ArrayList<>());
    }

    @Override
    public List<AuthorityDto> authorities() {
        return new ArrayList<>(authorities);
    }

    public List<AuthorityDto> meaningfulAuthorities() {
        List<AuthorityDto> output = new ArrayList<>();
        for (AuthorityDto authority : authorities) {
            overwriteAuthority(output, authority);
        }
        return output;
    }

    public UserDto withUniversity(@NonNull UniversityDto university) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withStudentId(@NonNull String studentId) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withName(@NonNull String name) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withToken(@NonNull String token) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withCreatedAt(long createdAt) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withApprovedAt(long approvedAt) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withAuthorities(@NonNull List<AuthorityDto> authorities) {
        return new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
    }

    public UserDto withAuthorityAdd(AuthorityDto authorityDto) {
        UserDto output = new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
        output.authorities.add(authorityDto);
        return output;
    }

    public UserDto withAuthorityRemove(DepartmentDto department) {
        UserDto output = new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);
        output.authorities.removeIf(
                (authority) -> department.matchUniqueKey(authority.department()));
        return output;
    }

    public UserDto withAuthorityUpdate(DepartmentDto department, Permission permission) {
        UserDto output = new UserDto(university, studentId, name, token, createdAt, approvedAt, authorities);

        if (permission == null) {
            output.authorities.removeIf(
                    (authority) -> department.matchUniqueKey(authority.department()));
            return output;
        }

        for (int i = 0; i < output.authorities.size(); i++) {
            AuthorityDto authority = output.authorities.get(i);
            if (authority.department().matchUniqueKey(department)) {
                output.authorities.set(i, new AuthorityDto(department, permission));
                return output;
            }
        }

        output.authorities.add(new AuthorityDto(department, permission));
        return output;
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "UserDto{" +
                "university=" + university +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", approvedAt=" + approvedAt +
                ", authorities=" + authorities +
                '}';
    }

    public boolean matchUniqueKey(String universityCode, String studentId) {
        if (universityCode == null || studentId == null) {
            return false;
        }
        return universityCode.equals(this.university().code())
                && studentId.equals(this.studentId());
    }

    public boolean matchUniqueKey(UserDto oth) {
        if (oth == null) {
            return false;
        }
        String universityCode = oth.university().code();
        String studentId = oth.studentId();
        return universityCode.equals(this.university().code())
                && studentId.equals(this.studentId());
    }

    public boolean isDeveloper() {
        for (AuthorityDto authority : authorities) {
            if (authority.permission().hasDeveloperPermission()) {
                return true;
            }
        }
        return false;
    }

    public Permission getMaxPermission(DepartmentDto department) {
        if (isDeveloper()) return Permission.DEVELOPER;

        Permission maxPermission = Permission.BANNED;
        for (AuthorityDto authority : authorities) {
            if (department.equals(authority.department())
                    && authority.permission() == Permission.DEFAULT) {
                maxPermission = Permission.USER;
                break;
            }
        }

        for (AuthorityDto authority : authorities) {
            if (department.equals(authority.department())
                    && authority.permission() != Permission.DEFAULT) {
                maxPermission = authority.permission();
                break;
            }
        }
        return maxPermission;
    }

    private void overwriteAuthority(List<AuthorityDto> list, AuthorityDto newAuthority) {
        DepartmentDto targetDepartment = newAuthority.department();
        Permission newPermission = newAuthority.permission();
        if (newPermission == Permission.DEFAULT) {
            newPermission = Permission.USER;
        }

        boolean notExist = true;
        for (int i = 0; i < list.size(); i++) {
            DepartmentDto department = list.get(i).department();
            if (!department.matchUniqueKey(targetDepartment)) continue;
            notExist = false;

            if (newAuthority.permission() != Permission.DEFAULT) {
                list.set(i, new AuthorityDto(department, newPermission));
            }
            break;
        }
        if (notExist) list.add(new AuthorityDto(targetDepartment, newPermission));
    }

    private static long currentTime() {
        return System.currentTimeMillis() / 1000;
    }

    private static String newToken() {
        return UUID.randomUUID().toString();
    }
}
