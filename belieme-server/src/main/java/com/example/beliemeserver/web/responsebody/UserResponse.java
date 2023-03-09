package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.UserDto;
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
    private long createTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long approvalTimeStamp;

    private UserResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private UserResponse(UniversityResponse university, String studentId, String name, List<AuthorityResponse> authorities, String token, long createTimeStamp, long approvalTimeStamp) {
        super(true);
        this.university = university;
        this.studentId = studentId;
        this.name = name;
        this.authorities = authorities;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
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
                userDto.createTimeStamp(),
                userDto.approvalTimeStamp()
        );
    }

    public UserResponse withoutSecureInfo() {
        return new UserResponse(university, studentId, name, null, null, 0, 0);
    }
}
