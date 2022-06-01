package com.example.beliemeserver.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
public class StuffDto {
    private String name;
    private String emoji;
    private List<ItemDto> items;

    public int getAmount() {
        int amount = 0;
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getState() == ItemDto.ItemStatus.USABLE || items.get(i).getState() == ItemDto.ItemStatus.UNUSABLE) {
                amount++;
            }
        }
        return amount;
    }

    public int getCount() {
        int count = 0;
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getState() == ItemDto.ItemStatus.USABLE) {
                count++;
            }
        }
        return count;
    }
}
