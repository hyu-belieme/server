package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class NotFoundException extends ServerException {

  public NotFoundException() {
    super(CommonErrorInfo.NOT_FOUND);
  }
}
