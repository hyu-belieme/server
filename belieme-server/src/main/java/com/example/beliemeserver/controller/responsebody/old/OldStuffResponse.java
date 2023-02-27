package com.example.beliemeserver.controller.responsebody.old;

import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.dto.old.OldStuffDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class OldStuffResponse extends OldJSONResponse {
    private String name;
    private String emoji;
    private int amount;
    private int count;
//    private int nextItemNum;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OldItemResponse> itemList;


    public OldStuffResponse(String name, String emoji, int amount, int count, List<OldItemResponse> itemList) {
        super(true);
        this.name = name;
        this.emoji = emoji;
        this.amount = amount;
        this.count = count;
        this.itemList = itemList;
    }

//    public StuffResponse(String name, String emoji, int amount, int count, List<ItemResponse> itemList, int nextItemNum) {
//        super(true);
//        this.name = name;
//        this.emoji = emoji;
//        this.amount = amount;
//        this.count = count;
//        this.itemList = itemList;
//        this.nextItemNum = nextItemNum;
//    }

    private OldStuffResponse(boolean doesJsonInclude) {
        super(false);
    }

    public static OldStuffResponse responseWillBeIgnore() {
        return new OldStuffResponse(false);
    }

    public static OldStuffResponse from(OldStuffDto stuffDto) {
        List<OldItemResponse> itemResponseList = new ArrayList<>();

        List<OldItemDto> itemDtoList = stuffDto.getItems();
        for(int i = 0; i < itemDtoList.size(); i++) {
            itemResponseList.add(OldItemResponse.from(itemDtoList.get(i)));
        }
        return new OldStuffResponse(stuffDto.getName(), stuffDto.getEmoji(), stuffDto.getAmount(), stuffDto.getCount(), itemResponseList);
    }

    public OldStuffResponse toStuffResponseWithoutItemList() {
        return new OldStuffResponse(name, emoji, amount, count, null);
    }
}
