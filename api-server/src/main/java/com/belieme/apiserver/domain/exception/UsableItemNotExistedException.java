package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class UsableItemNotExistedException extends ForbiddenException {

  @Override
  public String getName() {
    return "USABLE_ITEM_NOT_EXISTED";
  }

  @Override
  public Message getResponseMessage() {
    return new Message("message.error.usableItemNotExisted");
  }
}
