package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class ForbiddenException extends ServerException {
    public ForbiddenException() {
        super(CommonErrorInfo.FORBIDDEN);
    }
}
