package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class InvalidIndexException extends ServerException {
    public InvalidIndexException() {
        super(CommonErrorInfo.BAD_REQUEST);
    }
}
