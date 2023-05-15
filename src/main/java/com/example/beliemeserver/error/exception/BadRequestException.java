package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class BadRequestException extends ServerException {
    public BadRequestException() {
        super(CommonErrorInfo.BAD_REQUEST);
    }
}
