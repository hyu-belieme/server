package com.example.beliemeserver.controller.responsebody.old;

import com.example.beliemeserver.model.dto.old.OldHistoryDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"item", "num", "status", "reservedTimeStamp", "requester", "approveTimeStamp", "approveManager", "lostTimeStamp", "lostManager", "returnTimeStamp", "returnManager", "cancelTimeStamp", "cancelManager"})
public class OldHistoryResponse extends OldJSONResponse {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = OldResponseFilter.class)
    private OldItemResponse item;

    private int num;

    private OldUserResponse requester;
    private OldUserResponse approveManager;
    private OldUserResponse returnManager;
    private OldUserResponse lostManager;
    private OldUserResponse cancelManager;

    private long reservedTimeStamp;
    private long approveTimeStamp;
    private long returnTimeStamp;
    private long lostTimeStamp;
    private long cancelTimeStamp;
    private String status;

    public OldHistoryResponse(OldItemResponse item, int num, OldUserResponse requester, OldUserResponse approveManager, OldUserResponse returnManager, OldUserResponse lostManager, OldUserResponse cancelManager, long reservedTimeStamp, long approveTimeStamp, long returnTimeStamp, long lostTimeStamp, long cancelTimeStamp, String status) {
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

    private OldHistoryResponse(boolean doesJsonInclude) {
        super(false);
    }

    public static OldHistoryResponse responseWillBeIgnore() {
        return new OldHistoryResponse(false);
    }

    public OldHistoryResponse toHistoryResponseNestedInItem() {
        return new OldHistoryResponse(OldItemResponse.responseWillBeIgnore(), num, requester, approveManager, returnManager, lostManager, cancelManager, reservedTimeStamp, approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }

    public OldHistoryResponse toHistoryResponseWithItemWithoutLastHistory() {
        return new OldHistoryResponse(item.toItemResponseWithoutLastHistory(), num, requester, approveManager, returnManager, lostManager, cancelManager, reservedTimeStamp, approveTimeStamp, returnTimeStamp, lostTimeStamp, cancelTimeStamp, status);
    }

    public static OldHistoryResponse from(OldHistoryDto historyDto) {
        OldHistoryResponse historyResponse = new OldHistoryResponse(
                OldItemResponse.responseWillBeIgnore(), historyDto.getNum(), null, null,
                null, null, null, historyDto.getReservedTimeStamp(),
                historyDto.getApproveTimeStamp(), historyDto.getReturnTimeStamp(), historyDto.getLostTimeStamp(),
                historyDto.getCancelTimeStamp(), historyDto.getStatus().toString());
        if(historyDto.getItem() != null) {
            historyResponse.setItem(OldItemResponse.from(historyDto.getItem()));
        }

        if(historyDto.getRequester() != null) {
            historyResponse.setRequester(OldUserResponse.from(historyDto.getRequester()).toUserResponseNestedInHistory());
        }
        if(historyDto.getApproveManager() != null) {
            historyResponse.setApproveManager(OldUserResponse.from(historyDto.getApproveManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getReturnManager() != null) {
            historyResponse.setReturnManager(OldUserResponse.from(historyDto.getReturnManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getLostManager() != null) {
            historyResponse.setLostManager(OldUserResponse.from(historyDto.getLostManager()).toUserResponseNestedInHistory());
        }
        if(historyDto.getCancelManager() != null) {
            historyResponse.setCancelManager(OldUserResponse.from(historyDto.getCancelManager()).toUserResponseNestedInHistory());
        }
        return historyResponse;
    }
}
