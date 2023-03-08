package com.example.beliemeserver.exception;

import com.example.beliemeserver.common.Message;
import org.springframework.http.HttpStatus;

public enum CommonErrorInfo implements ErrorInfo {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, new Message("error.badRequest")),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, new Message("error.unauthorized")),
    FORBIDDEN(HttpStatus.FORBIDDEN, new Message("error.forbidden")),
    NOT_FOUND(HttpStatus.NOT_FOUND, new Message("error.notFound")),
    CONFLICT(HttpStatus.CONFLICT, new Message("error.conflict")),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, new Message("error.internalServerError")),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, new Message("error.badGateway"));

    private final HttpStatus httpStatus;
    private final Message responseMessage;

    CommonErrorInfo(HttpStatus httpStatus, Message responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
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
