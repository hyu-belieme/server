package com.example.beliemeserver.exception;

import com.example.beliemeserver.model.dto.AuthorityDto;

public class ForbiddenException extends InternalServerException {
    private final AuthorityDto.Permission given;

    private final AuthorityDto.Permission required;

    private String KR_MESSAGE() {
        if(required == AuthorityDto.Permission.DEVELOPER) {
            return "해당 작업을 위해서는 최소 `" + required + "`의 권한이 필요합니다.";
        }
        return "당신은 해당 `department`에 대해 `" + given + "`의 권한을 갖고 있습니다."
                + "해당 작업을 위해서는 최소 `" + required + "`의 권한이 필요합니다.";
    }

    public ForbiddenException(AuthorityDto.Permission given, AuthorityDto.Permission required) {
        this.required = required;
        this.given = given;
        setMessage(KR_MESSAGE());
    }
}
