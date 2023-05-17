package com.belieme.apiserver.web.exception;

import com.belieme.apiserver.error.exception.BadRequestException;
import com.belieme.apiserver.util.message.Message;

public class InvalidUuidFormatException extends BadRequestException {

  @Override
  public String getName() {
    return "INVALID_UUID_FORMAT";
  }

  @Override
  public Message getResponseMessage() {
    return new Message("message.error.invalidUuidFormat");
  }
}
