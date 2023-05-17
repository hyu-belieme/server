package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class ConflictException extends ServerException {
    public ConflictException() {
        super(CommonErrorInfo.CONFLICT);
    }
}
