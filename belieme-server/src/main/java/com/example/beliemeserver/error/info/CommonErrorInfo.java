package com.example.beliemeserver.error.info;

import com.example.beliemeserver.common.Message;
import org.springframework.http.HttpStatus;

public enum CommonErrorInfo implements ErrorInfo {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, new Message("error.badRequest.message")),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, new Message("error.unauthorized.message")),
    FORBIDDEN(HttpStatus.FORBIDDEN, new Message("error.forbidden.message")),
    NOT_FOUND(HttpStatus.NOT_FOUND, new Message("error.notFound.message")),
    CONFLICT(HttpStatus.CONFLICT, new Message("error.conflict.message")),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, new Message("error.internalServerError.message")),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, new Message("error.badGateway.message")),
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, new Message("error.serviceUnavailable.message"));

    private final HttpStatus httpStatus;
    private final Message responseMessage;

    CommonErrorInfo(HttpStatus httpStatus, Message responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
    }

    public static ErrorInfo getByHttpStatus(HttpStatus status) {
        for(ErrorInfo errorInfo : CommonErrorInfo.values()) {
            if(errorInfo.httpStatus().equals(status)) return errorInfo;
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
