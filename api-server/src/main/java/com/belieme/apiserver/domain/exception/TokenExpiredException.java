package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.UnauthorizedException;
import com.belieme.apiserver.util.message.Message;

public class TokenExpiredException extends UnauthorizedException {
    @Override
    public String getName() {
        return "TOKE_EXPIRED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("message.error.tokenExpired");
    }
}