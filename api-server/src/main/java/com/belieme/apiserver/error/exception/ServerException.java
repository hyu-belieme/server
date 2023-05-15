package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.util.message.Message;
import com.belieme.apiserver.error.info.ErrorInfo;
import org.springframework.http.HttpStatusCode;

public abstract class ServerException extends RuntimeException {
    private final ErrorInfo errorInfo;

    public ServerException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public final HttpStatusCode getHttpStatus() {
        return errorInfo.httpStatus();
    }

    public String getName() {
        return errorInfo.name();
    }

    public Message getResponseMessage() {
        return errorInfo.responseMessage();
    }
}
