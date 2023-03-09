package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class ForbiddenException extends ServerException {
    public ForbiddenException() {
        super(CommonErrorInfo.FORBIDDEN);
    }
}
