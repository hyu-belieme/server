package com.example.beliemeserver.exception;

public class ForbiddenException extends ServerException {
    protected String koreanMessage() {
        return "액세스가 거부되었습니다. 해당 작업을 위해 필요한 권한이 부족합니다.";
    }
}
