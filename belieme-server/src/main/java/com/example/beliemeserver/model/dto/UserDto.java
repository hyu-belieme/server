package model.dto;

import lombok.*;
import lombok.experimental.Accessors;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class UserDto {
    public enum Permissions {
        BANNED, USER, STAFF, MASTER, DEVELOPER;
    }

    private String studentId;
    private String name;
    private String token;
    private int createTimeStamp;
    private int approvalTimeStamp;
    private Permissions permission;
}
