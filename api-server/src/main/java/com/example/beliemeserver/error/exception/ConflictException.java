package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class ConflictException extends ServerException {
    public ConflictException() {
        super(CommonErrorInfo.CONFLICT);
    }
}
