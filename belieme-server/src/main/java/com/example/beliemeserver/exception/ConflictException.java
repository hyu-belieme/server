package com.example.beliemeserver.exception;

public class ConflictException extends InternalServerException {
    private String KR_MESSAGE() {
        return "현재 리소스 상태와 충돌로 인해 요청한 작업을 완료할 수 없습니다. " +
                "기존의 리소스 상태를 다시 한번 확인하고 요청하여 주십시오.";
    }

    public ConflictException() {
        setMessage(KR_MESSAGE());
    }
}
