package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class InternalServerException extends ServerException {
    public InternalServerException() {
        super(CommonErrorInfo.INTERNAL_SERVER_ERROR);
    }
}
