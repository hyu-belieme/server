package com.example.beliemeserver.exception;

public class InvalidIndexException extends InternalServerException {
    private String KR_MESSAGE() {
        return "제공된 인덱스가 잘못되었습니다. " +
                "요청을 확인하고 유효한 인덱스를 제공하여 다시 시도하여 주십시오.";
    }

    public InvalidIndexException() {
        setMessage(KR_MESSAGE());
    }
}
