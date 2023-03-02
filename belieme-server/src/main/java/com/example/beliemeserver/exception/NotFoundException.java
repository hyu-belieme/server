package com.example.beliemeserver.exception;

public class NotFoundException extends InternalServerException {
    private String KR_MESSAGE() {
        return "요청된 리소스를 찾을 수 없습니다. " +
                "요청한 URL 혹은 Request Body를 다시한번 확인하고 요청하여 주십시오.";
    }

    public NotFoundException() {
        setMessage(KR_MESSAGE());
    }
}
