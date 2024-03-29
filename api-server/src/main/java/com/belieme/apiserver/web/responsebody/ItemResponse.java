package com.belieme.apiserver.web.responsebody;

import com.belieme.apiserver.domain.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ItemResponse extends JsonResponse {

  private UUID id;
  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
  private UniversityResponse university;
  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
  private DepartmentResponse department;
  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
  private StuffResponse stuff;

  private int num;
  private String status;
  @JsonInclude(value = JsonInclude.Include.CUSTOM, valueFilter = ResponseFilter.class)
  HistoryResponse lastHistory;

  private ItemResponse(boolean doesJsonInclude) {
    super(doesJsonInclude);
  }

  private ItemResponse(UUID id, UniversityResponse university, DepartmentResponse department,
      StuffResponse stuff, int num, String status, HistoryResponse lastHistory) {
    super(true);
    this.id = id;
    this.university = university;
    this.department = department;
    this.stuff = stuff;
    this.num = num;
    this.status = status;
    this.lastHistory = lastHistory;
  }

  public static ItemResponse responseWillBeIgnore() {
    return new ItemResponse(false);
  }

  public static ItemResponse from(ItemDto itemDto) {
    if (itemDto == null) {
      return null;
    }
    if (itemDto.equals(ItemDto.nestedEndpoint)) {
      return responseWillBeIgnore();
    }

    return new ItemResponse(itemDto.id(),
        UniversityResponse.from(itemDto.stuff().department().university()),
        DepartmentResponse.from(itemDto.stuff().department()).withoutUniversity(),
        StuffResponse.from(itemDto.stuff()).withoutUniversityAndDepartment().withoutItems(),
        itemDto.num(), itemDto.status().toString(),
        toNestedResponse(HistoryResponse.from(itemDto.lastHistory())));
  }

  public ItemResponse withoutUniversityAndDepartment() {
    return new ItemResponse(id, UniversityResponse.responseWillBeIgnore(),
        DepartmentResponse.responseWillBeIgnore(), stuff, num, status, lastHistory);
  }

  public ItemResponse withoutStuffInfo() {
    return new ItemResponse(id, university, department, StuffResponse.responseWillBeIgnore(), num,
        status, lastHistory);
  }

  public ItemResponse withoutLastHistory() {
    return new ItemResponse(id, university, department, stuff, num, status,
        HistoryResponse.responseWillBeIgnore());
  }

  private static HistoryResponse toNestedResponse(HistoryResponse history) {
    if (history == null) {
      return null;
    }
    return history.withoutStuffAndItem().withoutUniversityAndDepartment();
  }
}
