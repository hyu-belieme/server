package com.example.beliemeserver.model.dto;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record UserDto(
        @NonNull UniversityDto university, @NonNull String studentId,
        @NonNull String name, @NonNull String token,
        long createTimeStamp, long approvalTimeStamp,
        @NonNull List<MajorDto> majors,
        @NonNull List<AuthorityDto> authorities
) {
    public static final UserDto nestedEndpoint = new UserDto(UniversityDto.nestedEndpoint, "-", "-", "", 0, 0, new ArrayList<>(), new ArrayList<>());

    public UserDto(
            @NonNull UniversityDto university, @NonNull String studentId,
            @NonNull String name, @NonNull String token,
            long createTimeStamp, long approvalTimeStamp,
            @NonNull List<MajorDto> majors, @NonNull List<AuthorityDto> authorities
    ) {
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.majors = new ArrayList<>(majors);
        this.authorities = new ArrayList<>(authorities);
    }

    public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId, @NonNull String name) {
        return new UserDto(university, studentId, name, newToken(),
                currentTimeStamp(), currentTimeStamp(), new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public List<MajorDto> majors() {
        return new ArrayList<>(majors);
    }

    @Override
    public List<AuthorityDto> authorities() {
        return new ArrayList<>(authorities);
    }

    public UserDto withUniversity(@NonNull UniversityDto university) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withStudentId(@NonNull String studentId) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withName(@NonNull String name) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withToken(@NonNull String token) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withCreateTimeStamp(long createTimeStamp) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withApprovalTimeStamp(long approvalTimeStamp) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withMajors(@NonNull List<MajorDto> majors) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withMajorAdd(MajorDto majorDto) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
        output.majors.add(majorDto);
        return output;
    }

    public UserDto withMajorRemove(MajorDto majorDto) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
        output.majors.remove(majorDto);
        return output;
    }

    public UserDto withAuthorities(@NonNull List<AuthorityDto> authorities) {
        return new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
    }

    public UserDto withAuthorityAdd(AuthorityDto authorityDto) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
        output.authorities.add(authorityDto);
        return output;
    }

    public UserDto withAuthorityRemove(AuthorityDto authorityDto) {
        UserDto output = new UserDto(university, studentId, name, token, createTimeStamp, approvalTimeStamp, majors, authorities);
        output.authorities.remove(authorityDto);
        return output;
    }

    public AuthorityDto.Permission getMaxPermission(DepartmentDto departmentDto) {
        // TODO Needs implement
        return AuthorityDto.Permission.USER;
    }

    private static long currentTimeStamp() {
        return System.currentTimeMillis()/1000;
    }

    private static String newToken() {
        return UUID.randomUUID().toString();
    }
}
