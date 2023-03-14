package com.example.beliemeserver.error.exception;

import com.example.beliemeserver.config.message.Message;
import com.example.beliemeserver.error.info.ErrorInfo;
import org.springframework.http.HttpStatus;

public abstract class ServerException extends RuntimeException {
    private final ErrorInfo errorInfo;

    public ServerException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public final HttpStatus getHttpStatus() {
        return errorInfo.httpStatus();
    }

    public String getName() {
        return errorInfo.name();
    }

    public Message getResponseMessage() {
        return errorInfo.responseMessage();
    }
}
