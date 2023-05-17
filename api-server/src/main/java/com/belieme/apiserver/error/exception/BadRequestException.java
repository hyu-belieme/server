package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class BadRequestException extends ServerException {
    public BadRequestException() {
        super(CommonErrorInfo.BAD_REQUEST);
    }
}
