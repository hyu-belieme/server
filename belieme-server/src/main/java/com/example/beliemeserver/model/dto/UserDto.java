package com.example.beliemeserver.model.dto;

import com.example.beliemeserver.data.entity.AuthorityEntity;
import com.example.beliemeserver.data.entity.UserEntity;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
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
    private Set<AuthorityDto> authorities;

    public UserDto() {
        authorities = new HashSet<>();
    }

    public AuthorityDto.Permission getMaxPermission() {
        AuthorityDto.Permission maxPermmision = null;
        Iterator<AuthorityDto> iterator = authorities.iterator();
        while(iterator.hasNext()) {
            AuthorityDto currentAuthorityDto = iterator.next();
            if(currentAuthorityDto.getPermission() == AuthorityDto.Permission.BANNED) {
                return AuthorityDto.Permission.BANNED;
            }
            if(maxPermmision == null || maxPermmision.hasMorePermission(currentAuthorityDto.getPermission())) {
                maxPermmision = currentAuthorityDto.getPermission();
            }
        }

        return maxPermmision;
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
