package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.HistoryDto;
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

    public HistoryResponse toHistoryResponseWithItemWithoutLastHistory() {
        return new HistoryResponse(item.toItemResponseWithoutLastHistory(), num, requester, approveManager, returnManager, lostManager, cancelManager, reservedTimeStamp, approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }

    public static HistoryResponse from(HistoryDto historyDto) {
        HistoryResponse historyResponse = new HistoryResponse(
                ItemResponse.responseWillBeIgnore(), historyDto.getNum(), null, null,
                null, null, null, historyDto.getReservedTimeStamp(),
                historyDto.getApproveTimeStamp(), historyDto.getReturnTimeStamp(), historyDto.getLostTimeStamp(),
                historyDto.getCancelTimeStamp(), historyDto.getStatus().toString());
        if(historyDto.getItem() != null) {
            historyResponse.setItem(ItemResponse.from(historyDto.getItem()));
        }

        if(historyDto.getRequester() != null) {
            historyResponse.setRequester(UserResponse.from(historyDto.getRequester()).toUserResponseNestedInHistory());
        }
        if(historyDto.getApproveManager() != null) {
            historyResponse.setApproveManager(UserResponse.from(historyDto.getApproveManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getReturnManager() != null) {
            historyResponse.setReturnManager(UserResponse.from(historyDto.getReturnManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getLostManager() != null) {
            historyResponse.setLostManager(UserResponse.from(historyDto.getLostManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getCancelManager() != null) {
            historyResponse.setCancelManager(UserResponse.from(historyDto.getCancelManager()).toUserResponseNestedInHistory());
        }
        return historyResponse;
    }
}
