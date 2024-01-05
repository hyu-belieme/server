package com.belieme.apiserver.web.responsebody;

import lombok.Getter;

@Getter
public class HealthResponse extends JsonResponse {

  private final String status = "UP";

  private HealthResponse(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  public HealthResponse() {
    super(true);
  }

  public static HealthResponse responseWillBeIgnore() {
    return new HealthResponse(false);
  }
}
