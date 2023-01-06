package com.example.beliemeserver.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class StuffDto {
    private DepartmentDto department;
    private String name;
    private String emoji;
    private final List<ItemDto> items;

    public static StuffDto init(DepartmentDto department, String name, String emoji) {
        return new StuffDto(department, name, emoji, new ArrayList<>());
    }

    public int getAmount() {
        int amount = 0;
        for (ItemDto item : items) {
            if (item.getStatus() == ItemDto.ItemStatus.USABLE
                    || item.getStatus() == ItemDto.ItemStatus.UNUSABLE) {
                amount++;
            }
        }
        return amount;
    }

    public int getCount() {
        int count = 0;
        for (ItemDto item : items) {
            if (item.getStatus() == ItemDto.ItemStatus.USABLE) {
                count++;
            }
        }
        return count;
    }
}
