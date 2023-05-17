package com.belieme.apiserver.domain.exception;

import com.belieme.apiserver.domain.util.Constants;
import com.belieme.apiserver.error.exception.ForbiddenException;
import com.belieme.apiserver.util.message.Message;

public class ItemAmountLimitExceededException extends ForbiddenException {

  @Override
  public String getName() {
    return "ITEM_AMOUNT_LIMIT_EXCEEDED";
  }

  @Override
  public Message getResponseMessage() {
    Object[] args = {Constants.MAX_ITEM_NUM};
    return new Message("message.error.itemAmountLimitExceeded", args);
  }
}
