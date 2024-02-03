package com.belieme.apiserver.web.responsebody;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"size", "next"})
public class CursorResponse extends JsonResponse {

  private int size;

  private String next;

  private CursorResponse(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  public CursorResponse(int size, String next) {
    super(true);
    this.size = size;
    this.next = next;
  }

  public static CursorResponse responseWillBeIgnore() {
    return new CursorResponse(false);
  }
}
