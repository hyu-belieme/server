package com.example.beliemeserver.exception;

public class BadGateWayException extends InternalServerException {
    private String KR_MESSAGE() {
        return "`upstream server` 또는 `gateway`에서 잘못된 응답을 수신했습니다. " +
                "나중에 다시 시도하거나 요청을 다시 한번 확인하여 주십시오.";
    }

    public BadGateWayException() {
        setMessage(KR_MESSAGE());
    }

    public BadGateWayException(String message) {
        super(message);
    }
}
