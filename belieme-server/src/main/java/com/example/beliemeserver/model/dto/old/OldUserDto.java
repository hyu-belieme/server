package com.example.beliemeserver.model.dto.old;

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
public class OldUserDto {
    private String studentId;
    private String name;
    private String token;
    private long createTimeStamp;
    private long approvalTimeStamp;
    private List<OldAuthorityDto> authorities;

    public OldUserDto() {
        authorities = new ArrayList<>();
    }

    public OldAuthorityDto.Permission getMaxPermission() {
        OldAuthorityDto.Permission maxPermission = null;
        Iterator<OldAuthorityDto> iterator = authorities.iterator();
        while(iterator.hasNext()) {
            OldAuthorityDto currentAuthorityDto = iterator.next();
            if(currentAuthorityDto.getPermission() == OldAuthorityDto.Permission.BANNED) {
                return OldAuthorityDto.Permission.BANNED;
            }
            if(maxPermission == null || maxPermission.hasMorePermission(currentAuthorityDto.getPermission())) {
                maxPermission = currentAuthorityDto.getPermission();
            }
        }
        return maxPermission;
    }

    public void addAuthority(OldAuthorityDto authorityDto) {
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
