package com.example.beliemeserver.exception;

public abstract class ServerException extends RuntimeException {
    @Override
    public String getMessage() {
        return koreanMessage();
    }

    protected abstract String koreanMessage();
}
