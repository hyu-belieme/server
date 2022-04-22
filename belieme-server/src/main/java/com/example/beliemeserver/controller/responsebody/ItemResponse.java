package com.example.beliemeserver.controller.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ItemResponse extends JSONResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String stuffName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String stuffEmoji;

    int num;

    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    HistoryResponse lastHistory;

    public ItemResponse(String stuffName, String stuffEmoji, int num, HistoryResponse lastHistory) {
        super(true);
        this.stuffName = stuffName;
        this.stuffEmoji = stuffEmoji;
        this.num = num;
        this.lastHistory = lastHistory;
    }

    private ItemResponse(boolean isNested) {
        super(false);
    }

    public static ItemResponse responseWillBeIgnore() {
        return new ItemResponse(false);
    }

    public ItemResponse toItemResponseNestedInHistory() {
        return new ItemResponse(stuffName, stuffEmoji, num, HistoryResponse.responseWillBeIgnore());
    }
}
