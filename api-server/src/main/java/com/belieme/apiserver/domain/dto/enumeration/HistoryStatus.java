package com.belieme.apiserver.domain.dto.enumeration;

public enum HistoryStatus {
  REQUESTED, USING, DELAYED, LOST, EXPIRED, RETURNED, FOUND, ERROR;

  public boolean dividedTo(HistoryStatus category) {
    if (category == USING) return (this == USING || this == DELAYED);
    if (category == RETURNED) return (this == RETURNED || this == FOUND);
    return this == category;
  }

  public boolean isClosed() {
    return (this == EXPIRED) || (this == RETURNED) || (this == FOUND);
  }

  public boolean isOpen() {
    return !isClosed() && (this != ERROR);
  }
}
