package com.example.beliemeserver.error.info;

import com.example.beliemeserver.config.message.Message;
import org.springframework.http.HttpStatus;

public enum CommonErrorInfo implements ErrorInfo {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, new Message("message.error.badRequest")),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, new Message("message.error.unauthorized")),
    FORBIDDEN(HttpStatus.FORBIDDEN, new Message("message.error.forbidden")),
    NOT_FOUND(HttpStatus.NOT_FOUND, new Message("message.error.notFound")),
    CONFLICT(HttpStatus.CONFLICT, new Message("message.error.conflict")),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, new Message("message.error.internalServerError")),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, new Message("message.error.badGateway")),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, new Message("message.error.serviceUnavailable"));

    private final HttpStatus httpStatus;
    private final Message responseMessage;

    CommonErrorInfo(HttpStatus httpStatus, Message responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
    }

    public static ErrorInfo getByHttpStatus(HttpStatus status) {
        for (ErrorInfo errorInfo : CommonErrorInfo.values()) {
            if (errorInfo.httpStatus().equals(status)) return errorInfo;
        }
        return INTERNAL_SERVER_ERROR;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public Message responseMessage() {
        return responseMessage;
    }
}
