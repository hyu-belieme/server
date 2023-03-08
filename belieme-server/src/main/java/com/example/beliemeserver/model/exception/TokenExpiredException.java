package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.common.Message;
import com.example.beliemeserver.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {
    @Override
    public String getName() {
        return "TOKE_EXPIRED";
    }

    @Override
    public Message getResponseMessage() {
        return new Message("error.tokenExpired.message");
    }
}