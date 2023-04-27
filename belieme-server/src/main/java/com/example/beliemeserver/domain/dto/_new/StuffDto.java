package com.example.beliemeserver.domain.dto._new;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record StuffDto(
        @NonNull UUID id, @NonNull DepartmentDto department,
        @NonNull String name, String thumbnail, @NonNull List<ItemDto> items
) {
    private static final UUID NIL_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final StuffDto nestedEndpoint = new StuffDto(NIL_UUID, DepartmentDto.nestedEndpoint, "-", "-", new ArrayList<>());

    public StuffDto(@NonNull UUID id, @NonNull DepartmentDto department, @NonNull String name, String thumbnail, @NonNull List<ItemDto> items) {
        this.id = id;
        this.department = department;
        this.name = name;
        this.thumbnail = thumbnail;
        this.items = new ArrayList<>(items);
    }

    public static StuffDto init(@NonNull DepartmentDto department, @NonNull String name, String thumbnail) {
        return new StuffDto(UUID.randomUUID(), department, name, thumbnail, new ArrayList<>());
    }

    @Override
    public List<ItemDto> items() {
        return new ArrayList<>(items);
    }

    public int itemsSize() {
        return items.size();
    }

    public StuffDto withDepartment(@NonNull DepartmentDto department) {
        return new StuffDto(id, department, name, thumbnail, items);
    }

    public StuffDto withName(@NonNull String name) {
        return new StuffDto(id, department, name, thumbnail, items);
    }

    public StuffDto withThumbnail(String thumbnail) {
        return new StuffDto(id, department, name, thumbnail, items);
    }

    public StuffDto withItems(@NonNull List<ItemDto> items) {
        return new StuffDto(id, department, name, thumbnail, items);
    }

    public StuffDto withItemAdd(ItemDto itemDto) {
        StuffDto output = new StuffDto(id, department, name, thumbnail, items);
        output.items.add(itemDto.withStuff(nestedEndpoint));

        return output;
    }

    public StuffDto withItemRemove(ItemDto itemDto) {
        StuffDto output = new StuffDto(id, department, name, thumbnail, items);
        output.items.removeIf(item -> item.matchUniqueKey(itemDto));

        return output;
    }

    public boolean matchUniqueKey(String universityName, String departmentName, String name) {
        return department().matchUniqueKey(universityName, departmentName)
                && this.name.equals(name);
    }

    public boolean matchUniqueKey(StuffDto oth) {
        if (oth == null) return false;
        return this.department.matchUniqueKey(oth.department())
                && this.name.equals(oth.name());
    }

    public int firstUsableItemNum() {
        for (ItemDto item : items) {
            if (item.isUsable()) return item.num();
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
        return items.size();
    }

    public int count() {
        return (int) items.stream().filter(ItemDto::isUsable).count();
    }

    @Override
    public String toString() {
        if (this.equals(nestedEndpoint)) {
            return "omitted";
        }
        return "StuffDto{" +
                "id=" + id +
                ", department=" + department +
                ", name='" + name + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", items=" + items +
                '}';
    }
}
