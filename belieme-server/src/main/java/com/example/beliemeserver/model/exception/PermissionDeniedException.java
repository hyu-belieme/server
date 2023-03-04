package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.ForbiddenException;

public class PermissionDeniedException extends ForbiddenException {
    @Override
    public String getName() {
        return "PERMISSION_DENIED";
    }

    @Override
    public String getMessage() {
        return "액세스가 거부되었습니다. 해당 작업을 위해 필요한 권한이 부족합니다.";
    }
}
