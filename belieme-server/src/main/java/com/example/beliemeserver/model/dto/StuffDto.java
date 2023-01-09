package com.example.beliemeserver.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Accessors(chain = true)
public class StuffDto {
    @NonNull
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private DepartmentDto department;

    @NonNull
    private String name;

    private String emoji;

    @NonNull
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private List<ItemDto> items;

    public StuffDto(@NonNull StuffDto stuffDto) {
        this.department = stuffDto.getDepartment();
        this.name = stuffDto.getName();
        this.emoji = stuffDto.getEmoji();
        this.items = stuffDto.getItems();
    }

    public StuffDto(@NonNull DepartmentDto department, @NonNull String name, String emoji, @NonNull List<ItemDto> items) {
        setDepartment(department);
        setName(name);
        setEmoji(emoji);
        setItems(items);
    }

    public static StuffDto init(@NonNull DepartmentDto department, @NonNull String name, String emoji) {
        return new StuffDto(department, name, emoji, new ArrayList<>());
    }

    public DepartmentDto getDepartment() {
        return new DepartmentDto(department);
    }

    public List<ItemDto> getItems() {
        List<ItemDto> output = new ArrayList<>();
        for(ItemDto item : items) {
            output.add(new ItemDto(item));
        }

        return output;
    }

    public StuffDto setDepartment(@NonNull DepartmentDto department) {
        this.department = new DepartmentDto(department);
        return this;
    }

    public StuffDto setItems(@NonNull List<ItemDto> items) {
        this.items = new ArrayList<>();
        for(ItemDto item : items) {
            addItem(new ItemDto(item));
        }

        return this;
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

    public void addItem(ItemDto item) {
        items.add(item);
    }
}
