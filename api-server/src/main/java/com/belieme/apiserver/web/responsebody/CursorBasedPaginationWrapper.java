package com.belieme.apiserver.web.responsebody;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"cursor", "data"})
public class CursorBasedPaginationWrapper<T> extends JsonResponse {

  private CursorResponse cursor;
  private List<T> data;

  private CursorBasedPaginationWrapper(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  public CursorBasedPaginationWrapper(List<T> data, int size, String next) {
    super(true);
    this.data = List.copyOf(data);
    this.cursor = new CursorResponse(size, next);
  }

  public static <T> CursorBasedPaginationWrapper<T> responseWillBeIgnore() {
    return new CursorBasedPaginationWrapper<>(false);
  }
}
