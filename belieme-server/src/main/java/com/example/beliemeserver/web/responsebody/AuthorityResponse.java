package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto._new.AuthorityDto;
import lombok.Getter;

@Getter
public class AuthorityResponse extends JsonResponse {
    private DepartmentResponse department;
    private String permission;

    private AuthorityResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private AuthorityResponse(DepartmentResponse department, String permission) {
        super(true);
        this.department = department;
        this.permission = permission;
    }

    public static AuthorityResponse responseWillBeIgnore() {
        return new AuthorityResponse(false);
    }

    public static AuthorityResponse from(AuthorityDto authorityDto) {
        if (authorityDto == null) return null;
        if (authorityDto.equals(AuthorityDto.nestedEndpoint)) {
            return responseWillBeIgnore();
        }

        return new AuthorityResponse(
                DepartmentResponse.from(authorityDto.department()),
                authorityDto.permission().toString()
        );
    }
}
