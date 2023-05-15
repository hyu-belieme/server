package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.error.info.CommonErrorInfo;

public class InvalidIndexException extends ServerException {
    public InvalidIndexException() {
        super(CommonErrorInfo.BAD_REQUEST);
    }
}
