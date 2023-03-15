package com.example.beliemeserver.domain.dto.enumeration;

public enum HistoryStatus {
    REQUESTED, USING, DELAYED, LOST, EXPIRED, RETURNED, FOUND, ERROR;

    public boolean isClosed() {
        return (this == EXPIRED) || (this == RETURNED) || (this == FOUND);
    }

    public boolean isOpen() {
        return !isClosed() && (this != ERROR);
    }
}
