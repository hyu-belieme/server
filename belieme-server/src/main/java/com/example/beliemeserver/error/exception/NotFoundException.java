package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class NotFoundException extends ServerException {
    public NotFoundException() {
        super(CommonErrorInfo.NOT_FOUND);
    }
}
