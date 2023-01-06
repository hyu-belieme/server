package com.example.beliemeserver.model.dto.old;

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
public class OldStuffDto {
    private String name;
    private String emoji;
    private List<OldItemDto> items;
    private int nextItemNum;

    public int getAmount() {
        int amount = 0;
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getStatus() == OldItemDto.ItemStatus.USABLE || items.get(i).getStatus() == OldItemDto.ItemStatus.UNUSABLE) {
                amount++;
            }
        }
        return amount;
    }

    public int getCount() {
        int count = 0;
        for(int i = 0; i < items.size(); i++) {
            if(items.get(i).getStatus() == OldItemDto.ItemStatus.USABLE) {
                count++;
            }
        }
        return count;
    }
}
