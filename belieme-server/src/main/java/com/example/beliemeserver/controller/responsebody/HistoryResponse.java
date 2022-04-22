package com.example.beliemeserver.controller.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"item", "num", "status", "reservedTimeStamp", "requester", "approveTimeStamp", "approveManager", "lostTimeStamp", "lostManager", "returnTimeStamp", "returnManager", "cancelTimeStamp", "cancelManager"})
public class HistoryResponse extends JSONResponse {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private ItemResponse item;

    private int num;

    private UserResponse requester;
    private UserResponse approveManager;
    private UserResponse returnManager;
    private UserResponse lostManager;
    private UserResponse cancelManager;

    private long reservedTimeStamp;
    private long approveTimeStamp;
    private long returnTimeStamp;
    private long lostTimeStamp;
    private long cancelTimeStamp;
    private String status;

    public HistoryResponse(ItemResponse item, int num, UserResponse requester, UserResponse approveManager, UserResponse returnManager, UserResponse lostManager, UserResponse cancelManager, long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp, long lostTimeStamp, long cancelTimeStamp, String status) {
        super(true);
        this.item = item;
        this.num = num;
        this.requester = requester;
        this.approveManager = approveManager;
        this.returnManager = returnManager;
        this.lostManager = lostManager;
        this.cancelManager = cancelManager;
        this.reservedTimeStamp = reservedTimeStamp;
        this.approveTimeStamp = approveTimeStamp;
        this.returnTimeStamp = returnTimeStamp;
        this.lostTimeStamp = lostTimeStamp;
        this.cancelTimeStamp = cancelTimeStamp;
        this.status = status;
    }

    private HistoryResponse(boolean doesJsonInclude) {
        super(false);
    }

    public static HistoryResponse responseWillBeIgnore() {
        return new HistoryResponse(false);
    }

    public HistoryResponse toHistoryResponseNestedInItem() {
        return new HistoryResponse(ItemResponse.responseWillBeIgnore(), num, requester, approveManager, returnManager, lostManager, cancelManager, reservedTimeStamp, approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }
}
