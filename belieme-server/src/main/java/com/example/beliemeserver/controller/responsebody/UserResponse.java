package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse extends JSONResponse {
    private String studentId;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long createTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long approvalTimeStamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String permission;

    public UserResponse(String studentId, String name, String token, long createTimeStamp, long approvalTimeStamp, String permission) {
        super(true);
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.permission = permission;
    }

    private UserResponse(boolean doesJsonInclude) {
        super(false);
    }

    public UserResponse toUserResponseNestedInHistory() {
        return new UserResponse(studentId, name, null, 0, 0, null);
    }

    public static UserResponse responseWillBeIgnore() {
        return new UserResponse(false);
    }

    public static UserResponse from(UserDto userDto) {
        if(userDto.getMaxPermission() == null) {
            return new UserResponse(
                    userDto.getStudentId(),
                    userDto.getName(),
                    userDto.getToken(),
                    userDto.getCreateTimeStamp(),
                    userDto.getApprovalTimeStamp(),
                    null
            );
        }
        return new UserResponse(
                userDto.getStudentId(),
                userDto.getName(),
                userDto.getToken(),
                userDto.getCreateTimeStamp(),
                userDto.getApprovalTimeStamp(),
                userDto.getMaxPermission().toString()
        );
    }
}
