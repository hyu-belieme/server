package com.example.beliemeserver.exception;

public class BadGateWayException extends ServerException {
    @Override
    protected String koreanMessage() {
        return "`upstream server` 또는 `gateway`에서 잘못된 응답을 수신했습니다. " +
                "나중에 다시 시도하거나 요청을 다시 한번 확인하여 주십시오.";
    }
}
