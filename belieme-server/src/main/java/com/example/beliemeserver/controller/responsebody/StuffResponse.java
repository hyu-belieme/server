package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
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

    public static StuffResponse from(StuffDto stuffDto) {
        List<ItemResponse> itemResponseList = new ArrayList<>();

        List<ItemDto> itemDtoList = stuffDto.getItems();
        for(int i = 0; i < itemResponseList.size(); i++) {
            itemResponseList.add(ItemResponse.from(itemDtoList.get(i)));
        }
        return new StuffResponse(stuffDto.getName(), stuffDto.getEmoji(), stuffDto.getAmount(), stuffDto.getCount(), itemResponseList);
    }

    public StuffResponse toStuffResponseWithoutItemList() {
        return new StuffResponse(name, emoji, amount, count, null);
    }
}
