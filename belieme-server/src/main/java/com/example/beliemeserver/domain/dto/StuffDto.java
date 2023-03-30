package com.example.beliemeserver.domain.dto;

import com.example.beliemeserver.domain.dto.enumeration.ItemStatus;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public record StuffDto(
        @NonNull DepartmentDto department, @NonNull String name,
        String thumbnail, @NonNull List<ItemDto> items
) {
    public static final StuffDto nestedEndpoint = new StuffDto(DepartmentDto.nestedEndpoint, "-", "-", new ArrayList<>());

    public StuffDto(@NonNull DepartmentDto department, @NonNull String name, String thumbnail, @NonNull List<ItemDto> items) {
        this.department = department;
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>(items);
    }

    public static StuffDto init(@NonNull DepartmentDto department, @NonNull String name, String thumbnail) {
        return new StuffDto(department, name, thumbnail, new ArrayList<>());
    }

    @Override
    public List<ItemDto> items() {
        return new ArrayList<>(items);
    }

    public int itemsSize() {
        return items.size();
    }

    public StuffDto withDepartment(@NonNull DepartmentDto department) {
        return new StuffDto(department, name, thumbnail, items);
    }

    public StuffDto withName(@NonNull String name) {
        return new StuffDto(department, name, thumbnail, items);
    }

    public StuffDto withThumbnail(String thumbnail) {
        return new StuffDto(department, name, thumbnail, items);
    }

    public StuffDto withItems(@NonNull List<ItemDto> items) {
        return new StuffDto(department, name, thumbnail, items);
    }

    public StuffDto withItemAdd(ItemDto itemDto) {
        StuffDto output = new StuffDto(department, name, thumbnail, items);
        output.items.add(itemDto.withStuff(nestedEndpoint));

        return output;
    }

    public StuffDto withItemRemove(ItemDto itemDto) {
        StuffDto output = new StuffDto(department, name, thumbnail, items);
        output.items.removeIf(item -> item.matchUniqueKey(itemDto));

        return output;
    }

    public boolean matchUniqueKey(String universityCode, String departmentCode, String name) {
        if (name == null) return false;
        return department().matchUniqueKey(universityCode, departmentCode)
                && name.equals(this.name());
    }

    public boolean matchUniqueKey(StuffDto oth) {
        if (oth == null) return false;
        return matchUniqueKey(
                oth.department().university().code(),
                oth.department().code(),
                oth.name()
        );
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }

        return "StuffDto{" +
                "department=" + department +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", items=" + items +
                '}';
    }

    public int firstUsableItemNum() {
        for (ItemDto item : items) {
            if (item.status() == ItemStatus.USABLE)
                return item.num();
        }
        return 0;
    }

    public int nextItemNum() {
        int nextItemNum = 0;
        for (ItemDto item : items) {
            if (item.num() > nextItemNum) {
                nextItemNum = item.num();
            }
        }
        return ++nextItemNum;
    }

    public int amount() {
        int amount = 0;
        for (ItemDto item : items) {
            if (item.status() == ItemStatus.USABLE
                    || item.status() == ItemStatus.UNUSABLE) {
                amount++;
            }
        }
        return amount;
    }

    public int count() {
        int count = 0;
        for (ItemDto item : items) {
            if (item.status() == ItemStatus.USABLE) {
                count++;
            }
        }
        return count;
    }
}
