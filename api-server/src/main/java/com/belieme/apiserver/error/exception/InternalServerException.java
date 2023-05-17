package com.belieme.apiserver.error.exception;

import com.belieme.apiserver.error.info.CommonErrorInfo;

public class InternalServerException extends ServerException {

  public InternalServerException() {
    super(CommonErrorInfo.INTERNAL_SERVER_ERROR);
  }
}
