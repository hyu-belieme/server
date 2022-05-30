package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class UserDto {
    private String studentId;
    private String name;
    private String token;
    private long createTimeStamp;
    private long approvalTimeStamp;
    private List<AuthorityDto> authorities;

    public UserDto() {
        authorities = new ArrayList<>();
    }

    public AuthorityDto.Permission getMaxPermission() {
        AuthorityDto.Permission maxPermission = null;
        Iterator<AuthorityDto> iterator = authorities.iterator();
        while(iterator.hasNext()) {
            AuthorityDto currentAuthorityDto = iterator.next();
            if(currentAuthorityDto.getPermission() == AuthorityDto.Permission.BANNED) {
                return AuthorityDto.Permission.BANNED;
            }
            if(maxPermission == null || maxPermission.hasMorePermission(currentAuthorityDto.getPermission())) {
                maxPermission = currentAuthorityDto.getPermission();
            }
        }
        return maxPermission;
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

    public void resetToken() {
        this.token = null;
    }
}
