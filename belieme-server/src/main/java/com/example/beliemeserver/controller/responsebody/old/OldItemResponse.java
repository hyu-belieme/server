package com.example.beliemeserver.controller.responsebody.old;

import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OldItemResponse extends OldJSONResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String stuffName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String stuffEmoji;

    int num;

    String status;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = OldResponseFilter.class)
    OldHistoryResponse lastHistory;

    public OldItemResponse(String stuffName, String stuffEmoji, int num, OldHistoryResponse lastHistory, String status) {
        super(true);
        this.stuffName = stuffName;
        this.stuffEmoji = stuffEmoji;
        this.num = num;
        this.lastHistory = lastHistory;
        this.status = status;
    }

    private OldItemResponse(boolean isNested) {
        super(false);
    }

    public static OldItemResponse responseWillBeIgnore() {
        return new OldItemResponse(false);
    }

    public OldItemResponse toItemResponseWithoutLastHistory() {
        return new OldItemResponse(stuffName, stuffEmoji, num, OldHistoryResponse.responseWillBeIgnore(), status);
    }

    public static OldItemResponse from(OldItemDto itemDto) {
        OldItemResponse itemResponse = new OldItemResponse(null, null, itemDto.getNum(), null, itemDto.getStatus().toString());
        if(itemDto.getStuff() != null) {
            itemResponse.setStuffName(itemDto.getStuff().getName());
            itemResponse.setStuffEmoji(itemDto.getStuff().getEmoji());
        }

        if(itemDto.getLastHistory() != null) {
            itemResponse.setLastHistory(OldHistoryResponse.from(itemDto.getLastHistory()));
        }
        return itemResponse;
    }
}
