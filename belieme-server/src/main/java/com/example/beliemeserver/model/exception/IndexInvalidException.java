package com.example.beliemeserver.model.exception;

import com.example.beliemeserver.exception.BadRequestException;

public class IndexInvalidException extends BadRequestException {
    @Override
    public String getName() {
        return "INDEX_INVALID";
    }

    @Override
    public String getMessage() {
        return "제공된 인덱스가 잘못되었습니다. 요청을 확인하고 유효한 인덱스를 제공하여 다시 시도하여 주십시오.";
    }
}
