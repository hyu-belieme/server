package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StuffResponse extends JsonResponse {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private UniversityResponse university;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private DepartmentResponse department;
    private String name;
    private String thumbnail;
    private int amount;
    private int count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemResponse> items;

    private StuffResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private StuffResponse(UniversityResponse university, DepartmentResponse department, String name, String thumbnail, int amount, int count, List<ItemResponse> items) {
        super(true);
        this.university = university;
        this.department = department;
        this.name = name;
        this.thumbnail = thumbnail;
        this.amount = amount;
        this.count = count;
        this.items = items;
    }

    public static StuffResponse responseWillBeIgnore() {
        return new StuffResponse(false);
    }

    public static StuffResponse from(StuffDto stuffDto) {
        List<ItemResponse> itemResponseList = new ArrayList<>();

        List<ItemDto> itemDtoList = stuffDto.items();
        for (ItemDto itemDto : itemDtoList) {
            ItemResponse nestedItemResponse = ItemResponse.from(itemDto)
                    .withoutUniversityAndDepartment()
                    .withoutStuffInfo();
            itemResponseList.add(nestedItemResponse);
        }
        return new StuffResponse(
                UniversityResponse.from(stuffDto.department().university()),
                DepartmentResponse.from(stuffDto.department()).withoutUniversity(),
                stuffDto.name(), stuffDto.thumbnail(), stuffDto.amount(),
                stuffDto.count(), itemResponseList
        );
    }

    public StuffResponse withoutUniversityAndDepartment() {
        return new StuffResponse(
                UniversityResponse.responseWillBeIgnore(),
                DepartmentResponse.responseWillBeIgnore(),
                name, thumbnail, amount, count, items);
    }

    public StuffResponse withoutItems() {
        return new StuffResponse(
                university, department, name, thumbnail,
                amount, count, null);
    }
}
