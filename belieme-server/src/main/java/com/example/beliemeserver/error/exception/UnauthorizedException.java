package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class UnauthorizedException extends ServerException {
    public UnauthorizedException() {
        super(CommonErrorInfo.UNAUTHORIZED);
    }
}
