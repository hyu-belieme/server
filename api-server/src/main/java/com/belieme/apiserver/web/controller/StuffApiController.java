package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.StuffDto;
import com.belieme.apiserver.domain.service.HistoryService;
import com.belieme.apiserver.domain.service.StuffService;
import com.belieme.apiserver.web.requestbody.StuffRequest;
import com.belieme.apiserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.belieme.apiserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import com.belieme.apiserver.web.responsebody.HistoryResponse;
import com.belieme.apiserver.web.responsebody.StuffResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class StuffApiController extends BaseApiController {

  private final StuffService stuffService;
  private final HistoryService historyService;

  public StuffApiController(StuffService stuffService, HistoryService historyService) {
    this.stuffService = stuffService;
    this.historyService = historyService;
  }

  @GetMapping("/${api.keyword.stuff}")
  public ResponseEntity<List<StuffResponse>> getAllStuffListOfDepartment(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestParam(value = "${api.query.department-id}") String departmentId) {
    List<StuffDto> stuffDtoList = stuffService.getListByDepartment(userToken, toUUID(departmentId));
    List<StuffResponse> responseList = toResponseList(stuffDtoList);
    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}")
  public ResponseEntity<StuffResponse> getStuff(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params) {
    UUID stuffId = toUUID(params.get(api.getVariable().stuffIndex()));

    StuffDto stuffDto = stuffService.getById(userToken, stuffId);
    StuffResponse response = StuffResponse.from(stuffDto);
    return ResponseEntity.ok(response);
  }

  @PostMapping("/${api.keyword.stuff}")
  public ResponseEntity<StuffResponse> createNewStuff(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestBody @Validated(StuffCreateValidationGroup.class) StuffRequest newStuff) {
    System.out.println(toUUID(newStuff.getDepartmentId()));
    StuffDto stuffDto = stuffService.create(userToken, toUUID(newStuff.getDepartmentId()),
        newStuff.getName(), newStuff.getThumbnail(), newStuff.getDesc(), newStuff.getAmount());
    StuffResponse response = StuffResponse.from(stuffDto);
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}")
  public ResponseEntity<StuffResponse> updateStuff(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params,
      @RequestBody @Validated(StuffUpdateValidationGroup.class) StuffRequest newStuff) {
    UUID stuffId = toUUID(params.get(api.getVariable().stuffIndex()));

    StuffDto stuffDto = stuffService.update(userToken, stuffId, newStuff.getName(),
        newStuff.getThumbnail(), newStuff.getDesc());
    StuffResponse response = StuffResponse.from(stuffDto);
    return ResponseEntity.ok(response);
  }

  private List<StuffResponse> toResponseList(List<StuffDto> stuffDtoList) {
    List<StuffResponse> responseList = new ArrayList<>();
    for (StuffDto dto : stuffDtoList) {
      responseList.add(StuffResponse.from(dto).withoutItems());
    }
    return responseList;
  }

  @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.do-reserve}")
  public ResponseEntity<HistoryResponse> reserveStuff(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params) {
    UUID stuffId = toUUID(params.get(api.getVariable().stuffIndex()));

    HistoryDto historyDto = historyService.createReservationOnStuff(userToken, stuffId);
    HistoryResponse response = HistoryResponse.from(historyDto);
    return ResponseEntity.ok(response);
  }
}
