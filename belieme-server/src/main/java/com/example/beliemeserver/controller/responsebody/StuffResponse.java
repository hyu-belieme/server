package com.example.beliemeserver.controller.responsebody;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class StuffResponse extends JSONResponse {
    private String name;
    private String emoji;
    private int amount;
    private int count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemResponse> itemList;


    public StuffResponse(String name, String emoji, int amount, int count, List<ItemResponse> itemList) {
        super(true);
        this.name = name;
        this.emoji = emoji;
        this.amount = amount;
        this.count = count;
        this.itemList = itemList;
    }

    private StuffResponse(boolean doesJsonInclude) {
        super(false);
    }

    public static StuffResponse responseWillBeIgnore() {
        return new StuffResponse(false);
    }
}
