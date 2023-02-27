package com.example.beliemeserver.model.dto;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public record UserDto(
        @NonNull UniversityDto university, @NonNull String studentId,
        @NonNull String name, @NonNull String token,
        long createTimeStamp, long approvalTimeStamp,
        @NonNull List<AuthorityDto> authorities
) {
    public static final UserDto nestedEndpoint = new UserDto(UniversityDto.nestedEndpoint, "-", "-", "", 0, 0, new ArrayList<>());

    public UserDto(
            @NonNull UniversityDto university, @NonNull String studentId,
            @NonNull String name, @NonNull String token,
            long createTimeStamp, long approvalTimeStamp,
            @NonNull List<AuthorityDto> authorities
    ) {
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.authorities = new ArrayList<>(authorities);
    }

    public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId, @NonNull String name) {
        return new UserDto(university, studentId, name, newToken(),
                currentTimeStamp(), currentTimeStamp(), new ArrayList<>());
    }

    @Override
    public List<AuthorityDto> authorities() {
        return new ArrayList<>(authorities);
    }

    public UserDto withUniversity(@NonNull UniversityDto university) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withStudentId(@NonNull String studentId) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withName(@NonNull String name) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withToken(@NonNull String token) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withCreateTimeStamp(long createTimeStamp) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withApprovalTimeStamp(long approvalTimeStamp) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withAuthorities(@NonNull List<AuthorityDto> authorities) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
    }

    public UserDto withAuthorityAdd(AuthorityDto authorityDto) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
        output.authorities.add(authorityDto);
        return output;
    }

    public UserDto withAuthorityRemove(DepartmentDto department) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);
        output.authorities.removeIf(
                (authority) -> department.matchUniqueKey(authority.department()));
        return output;
    }

    public UserDto withAuthorityUpdate(DepartmentDto department, AuthorityDto.Permission permission) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, authorities);

        if(permission == null) {
            output.authorities.removeIf(
                    (authority) -> department.matchUniqueKey(authority.department()));
            return output;
        }

        for(int i = 0; i < output.authorities.size(); i++) {
            AuthorityDto authority = output.authorities.get(i);
            if(authority.department().matchUniqueKey(department)) {
                output.authorities.set(i, new AuthorityDto(department, permission));
                return output;
            }
        }

        output.authorities.add(new AuthorityDto(department, permission));
        return output;
    }

    @Override
    public String toString() {
        if(this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "UserDto{" +
                "university=" + university +
                ", studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", token='" + token + '\'' +
                ", createTimeStamp=" + createTimeStamp +
                ", approvalTimeStamp=" + approvalTimeStamp +
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
        if(oth == null) {
            return false;
        }
        String universityCode = oth.university().code();
        String studentId = oth.studentId();
        return universityCode.equals(this.university().code())
                && studentId.equals(this.studentId());
    }

    public boolean isDeveloper() {
        for(AuthorityDto authority : authorities) {
            if(authority.permission().hasDeveloperPermission()) {
                return true;
            }
        }
        return false;
    }

    public AuthorityDto.Permission getMaxPermission(DepartmentDto department) {
        if(isDeveloper()) return AuthorityDto.Permission.DEVELOPER;

        AuthorityDto.Permission maxPermission = AuthorityDto.Permission.BANNED;
        for(AuthorityDto authority : authorities) {
            if(department.equals(authority.department())
                    && authority.permission() == AuthorityDto.Permission.DEFAULT) {
                maxPermission = AuthorityDto.Permission.USER;
                break;
            }
        }

        for(AuthorityDto authority : authorities) {
            if(department.equals(authority.department())
                    && authority.permission() != AuthorityDto.Permission.DEFAULT) {
                maxPermission = authority.permission();
                break;
            }
        }
        return maxPermission;
    }

    private static long currentTimeStamp() {
        return System.currentTimeMillis()/1000;
    }

    private static String newToken() {
        return UUID.randomUUID().toString();
    }
}
