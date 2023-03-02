package com.example.beliemeserver.exception;

public class NotFoundException extends ServerException {
    @Override
    protected String koreanMessage() {
        return "요청된 리소스를 찾을 수 없습니다. 요청을 다시 한번 확인하여 주십시오.";
    }
}
