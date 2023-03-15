package com.example.beliemeserver.web.responsebody;

import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class StuffResponse extends JsonResponse {
    private UniversityResponse university;
    private DepartmentResponse department;
    private String name;
    private String emoji;
    private int amount;
    private int count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ItemResponse> itemList;

    private StuffResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private StuffResponse(UniversityResponse university, DepartmentResponse department, String name, String emoji, int amount, int count, List<ItemResponse> itemList) {
        super(true);
        this.university = university;
        this.department = department;
        this.name = name;
        this.emoji = emoji;
        this.amount = amount;
        this.count = count;
        this.itemList = itemList;
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
                stuffDto.name(), stuffDto.emoji(), stuffDto.amount(),
                stuffDto.count(), itemResponseList
        );
    }
}
