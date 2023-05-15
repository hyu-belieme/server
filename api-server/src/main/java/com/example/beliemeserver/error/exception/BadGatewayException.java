package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class BadGatewayException extends ServerException {
    public BadGatewayException() {
        super(CommonErrorInfo.BAD_GATEWAY);
    }
}
