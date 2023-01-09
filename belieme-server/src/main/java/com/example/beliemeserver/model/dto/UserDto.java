package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class UserDto {
    @NonNull
    @Setter(AccessLevel.NONE)
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
    @Setter(AccessLevel.NONE)
    private List<MajorDto> majors;
    @NonNull
    @Setter(AccessLevel.NONE)
    private List<AuthorityDto> authorities;

    public UserDto(@NonNull UserDto userDto) {
        this.university = userDto.getUniversity();
        this.studentId = userDto.getStudentId();
        this.name = userDto.getName();
        this.token = userDto.getToken();
        this.createTimeStamp = userDto.getCreateTimeStamp();
        this.approvalTimeStamp = userDto.getApprovalTimeStamp();
        this.majors = userDto.getMajors();
        this.authorities = userDto.getAuthorities();
    }

    public UserDto(@NonNull UniversityDto university, @NonNull String studentId,
                   @NonNull String name, @NonNull String token,
                   long createTimeStamp, long approvalTimeStamp,
                   @NonNull List<MajorDto> majors, @NonNull List<AuthorityDto> authorities) {
        setUniversity(university);
        setStudentId(studentId);
        setName(name);
        setToken(token);
        setCreateTimeStamp(createTimeStamp);
        setApprovalTimeStamp(approvalTimeStamp);
        setMajors(majors);
        setAuthorities(authorities);
    }

    public static UserDto init(@NonNull UniversityDto university, @NonNull String studentId, @NonNull String name) {
        UserDto output = new UserDto(university, studentId, name, "",
                0, 0, new ArrayList<>(), new ArrayList<>());
        output.setCreateTimeStampNow();
        output.setApprovalTimeStampNow();
        output.setNewToken();
        return output;
    }

    public UniversityDto getUniversity() {
        return new UniversityDto(university);
    }

    public List<MajorDto> getMajors() {
        List<MajorDto> output = new ArrayList<>();
        for (MajorDto major : majors) {
            output.add(new MajorDto(major));
        }
        return output;
    }

    public List<AuthorityDto> getAuthorities() {
        List<AuthorityDto> output = new ArrayList<>();
        for (AuthorityDto authority : authorities) {
            output.add(new AuthorityDto(authority));
        }
        return output;
    }

    public UserDto setUniversity(@NonNull UniversityDto university) {
        this.university = new UniversityDto(university);
        return this;
    }

    public UserDto setMajors(@NonNull List<MajorDto> majors) {
        this.majors = new ArrayList<>();
        for (MajorDto major : majors) {
            addMajor(new MajorDto(major));
        }
        return this;
    }

    public UserDto setAuthorities(@NonNull List<AuthorityDto> authorities) {
        this.authorities = new ArrayList<>();
        for (AuthorityDto authority : authorities) {
            addAuthority(new AuthorityDto(authority));
        }
        return this;
    }

    public UserDto setCreateTimeStampNow() {
        createTimeStamp = System.currentTimeMillis()/1000;
        return this;
    }

    public UserDto setApprovalTimeStampNow() {
        approvalTimeStamp = System.currentTimeMillis()/1000;
        return this;
    }

    public UserDto setNewToken() {
        this.token = UUID.randomUUID().toString();
        return this;
    }

    public UserDto addMajor(MajorDto majorDto) {
        this.majors.add(majorDto);
        return this;
    }

    public UserDto addAuthority(AuthorityDto authorityDto) {
        authorities.add(authorityDto);
        return this;
    }

    public UserDto removeMajor(MajorDto majorDto) {
        this.majors.remove(majorDto);
        return this;
    }

    public UserDto removeAuthority(AuthorityDto authorityDto) {
        authorities.remove(authorityDto);
        return this;
    }

    public AuthorityDto.Permission getMaxPermission(DepartmentDto departmentDto) {
        // TODO Needs implement
        return AuthorityDto.Permission.USER;
    }
}
