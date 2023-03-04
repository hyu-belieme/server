package com.example.beliemeserver.exception;

import org.springframework.http.HttpStatus;

public enum CommonErrorInfo implements ErrorInfo {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청으로 전달 받은 변수들이 유효하지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "토큰 인증에 실패하였습니다. 유효한 토큰을 통해 접근해 주십시오."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "요청이 거부되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청된 리소스를 찾을 수 없습니다. 요청을 다시 한번 확인하여 주십시오."),
    CONFLICT(HttpStatus.CONFLICT, "현재 리소스 상태와 충돌로 인해 요청한 작업을 완료할 수 없습니다. 기존의 리소스 상태를 다시 한번 확인하고 요청하여 주십시오."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상하지 못한 문제가 서버 내부에 발생하였습니다. 나중에 다시 시도하여 주십시오."),
    BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "`upstream server` 또는 `gateway`에서 잘못된 응답을 수신했습니다. 나중에 다시 시도하거나 요청을 다시 한번 확인하여 주십시오.");

    private final HttpStatus httpStatus;
    private final String responseMessage;

    CommonErrorInfo(HttpStatus httpStatus, String responseMessage) {
        this.httpStatus = httpStatus;
        this.responseMessage = responseMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }

    @Override
    public String responseMessage() {
        return responseMessage;
    }
}
