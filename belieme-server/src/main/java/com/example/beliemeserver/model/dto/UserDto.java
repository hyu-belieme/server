package com.example.beliemeserver.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UserDto {
    @NonNull
    private UniversityDto university;
    @NonNull
    private String studentId;
    @NonNull
    private String name;
    @NonNull
    private String token;

    private long createTimeStamp;
    private long approvalTimeStamp;

    @NonNull
    private final List<MajorDto> majors;
    @NonNull
    private final List<AuthorityDto> authorities;

    public static UserDto init(UniversityDto university, String studentId, String name) {
        return new UserDto(university, studentId, name);
    }

    private UserDto(UniversityDto university, String studentId, String name) {
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.setNewToken();
        this.setCreateTimeStampNow();
        this.setApprovalTimeStampNow();
        majors = new ArrayList<>();
        authorities = new ArrayList<>();
    }

    public AuthorityDto.Permission getMaxPermission(DepartmentDto departmentDto) {
        // TODO Needs implement
        return AuthorityDto.Permission.USER;
    }

    public void addMajor(MajorDto majorDto) {
        this.majors.add(majorDto);
    }

    public void addAuthority(AuthorityDto authorityDto) {
        authorities.add(authorityDto);
    }

    public void setCreateTimeStampNow() {
        createTimeStamp = System.currentTimeMillis()/1000;
    }

    public void setApprovalTimeStampNow() {
        approvalTimeStamp = System.currentTimeMillis()/1000;
    }

    public void setNewToken() {
        this.token = UUID.randomUUID().toString();
    }
}
