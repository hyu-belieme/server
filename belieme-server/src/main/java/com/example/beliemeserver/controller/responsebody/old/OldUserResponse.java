package com.example.beliemeserver.controller.responsebody.old;

import com.example.beliemeserver.model.dto.old.OldUserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OldUserResponse extends OldJSONResponse {
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

    public OldUserResponse(String studentId, String name, String token, long createTimeStamp, long approvalTimeStamp, String permission) {
        super(true);
        this.studentId = studentId;
        this.name = name;
        this.token = token;
        this.createTimeStamp = createTimeStamp;
        this.approvalTimeStamp = approvalTimeStamp;
        this.permission = permission;
    }

    private OldUserResponse(boolean doesJsonInclude) {
        super(false);
    }

    public OldUserResponse toUserResponseNestedInHistory() {
        return new OldUserResponse(studentId, name, null, 0, 0, null);
    }

    public static OldUserResponse responseWillBeIgnore() {
        return new OldUserResponse(false);
    }

    public static OldUserResponse from(OldUserDto userDto) {
        if(userDto.getMaxPermission() == null) {
            return new OldUserResponse(
                    userDto.getStudentId(),
                    userDto.getName(),
                    userDto.getToken(),
                    userDto.getCreateTimeStamp(),
                    userDto.getApprovalTimeStamp(),
                    null
            );
        }
        return new OldUserResponse(
                userDto.getStudentId(),
                userDto.getName(),
                userDto.getToken(),
                userDto.getCreateTimeStamp(),
                userDto.getApprovalTimeStamp(),
                userDto.getMaxPermission().toString()
        );
    }
}
