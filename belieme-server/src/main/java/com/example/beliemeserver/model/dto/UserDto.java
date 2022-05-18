package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Set;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class UserDto {
    private String studentId;
    private String name;
    private String token;
    private int createTimeStamp;
    private int approvalTimeStamp;

    private Set<AuthorityDto> authorityDtoSet;
}
