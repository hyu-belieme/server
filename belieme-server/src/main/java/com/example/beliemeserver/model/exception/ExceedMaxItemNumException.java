package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.MethodNotAllowedException;

public class ExceedMaxItemNumException extends MethodNotAllowedException {
    @Override
    protected String koreanMessage() {
        return "";
    }
}
