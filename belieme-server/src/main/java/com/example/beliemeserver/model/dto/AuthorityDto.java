package com.example.beliemeserver.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class AuthorityDto {
    public enum Permissions {
        BANNED, USER, STAFF, MASTER, DEVELOPER;
    }
    private Permissions permission;
}
