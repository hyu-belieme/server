package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.AuthorityDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class UserResponse extends JsonResponse {
    private UniversityResponse university;
    private String studentId;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AuthorityResponse> authorities;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createdAt;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long approvedAt;

    private UserResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private UserResponse(UniversityResponse university, String studentId, String name, List<AuthorityResponse> authorities, String token, long createdAt, long approvedAt) {
        super(true);
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.authorities = authorities;
        this.token = token;
        this.createdAt = createdAt;
        this.approvedAt = approvedAt;
    }

    public static UserResponse responseWillBeIgnore() {
        return new UserResponse(false);
    }

    public static UserResponse from(UserDto userDto) {
        if (userDto == null) return null;
        if (userDto.equals(UserDto.nestedEndpoint)) {
            return responseWillBeIgnore();
        }

        List<AuthorityResponse> authorityResponseList = new ArrayList<>();
        for (AuthorityDto authorityDto : userDto.meaningfulAuthorities()) {
            authorityResponseList.add(AuthorityResponse.from(authorityDto));
        }

        return new UserResponse(
                UniversityResponse.from(userDto.university()),
                userDto.studentId(),
                userDto.name(),
                authorityResponseList,
                userDto.token(),
                userDto.createdAt(),
                userDto.approvedAt()
        );
    }

    public UserResponse withoutSecureInfo() {
        return new UserResponse(university, studentId, name, null, null, 0, 0);
    }
}
