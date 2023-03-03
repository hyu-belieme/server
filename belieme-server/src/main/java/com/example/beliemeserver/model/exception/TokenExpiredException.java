package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    @Override
    public String getName() {
        return "TOKEN_EXPIRED";
    }

    @Override
    public String getMessage() {
        return "토큰 인증에 실패하였습니다. 해당 토큰이 만료되었습니다.";
    }
}