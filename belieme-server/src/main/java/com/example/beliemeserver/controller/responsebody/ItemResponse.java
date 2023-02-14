package com.example.beliemeserver.controller.responsebody;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ItemResponse extends JsonResponse {
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private UniversityResponse university;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    private DepartmentResponse department;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stuffName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String stuffEmoji;
    private int num;
    private String status;
    @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
    HistoryResponse lastHistory;

    private ItemResponse(boolean doesJsonInclude) {
        super(doesJsonInclude);
    }

    private ItemResponse(UniversityResponse university, DepartmentResponse department, String stuffName, String stuffEmoji, int num, String status, HistoryResponse lastHistory) {
        super(true);
        this.university = university;
        this.department = department;
        this.stuffName = stuffName;
        this.stuffEmoji = stuffEmoji;
        this.num = num;
        this.status = status;
        this.lastHistory = lastHistory;
    }

    public static ItemResponse responseWillBeIgnore() {
        return new ItemResponse(false);
    }

    public static ItemResponse from(ItemDto itemDto) {
        if(itemDto == null) return null;
        if(itemDto.equals(ItemDto.nestedEndpoint)) {
            return responseWillBeIgnore();
        }

        String stuffName = null;
        String stuffEmoji = null;
        if(!itemDto.stuff().equals(StuffDto.nestedEndpoint)) {
            stuffName = itemDto.stuff().name();
            stuffEmoji = itemDto.stuff().emoji();
        }

        return new ItemResponse(
                UniversityResponse.from(itemDto.stuff().department().university()),
                DepartmentResponse.from(itemDto.stuff().department()).withoutUniversity(),
                stuffName,
                stuffEmoji,
                itemDto.num(),
                itemDto.status().toString(),
                toNestedResponse(HistoryResponse.from(itemDto.lastHistory()))
        );
    }

    public ItemResponse withoutUniversityAndDepartment() {
        return new ItemResponse(
                UniversityResponse.responseWillBeIgnore(),
                DepartmentResponse.responseWillBeIgnore(),
                stuffName, stuffEmoji, num, status, lastHistory);
    }

    public ItemResponse withoutStuffInfo() {
        return new ItemResponse(
                university, department, null, null,
                num, status, lastHistory);
    }

    public ItemResponse withoutLastHistory() {
        return new ItemResponse(
                university, department, stuffName,
                stuffEmoji, num, status, HistoryResponse.responseWillBeIgnore());
    }

    private static HistoryResponse toNestedResponse(HistoryResponse history) {
        if(history == null) return null;
        return history
                .withoutItem()
                .withoutUniversityAndDepartment();
    }
}
