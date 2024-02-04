package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.dto.HistoryCursorDto;
import com.belieme.apiserver.domain.dto.HistoryDto;
import com.belieme.apiserver.domain.dto.enumeration.HistoryStatus;
import com.belieme.apiserver.domain.service.HistoryService;
import com.belieme.apiserver.error.exception.BadRequestException;
import com.belieme.apiserver.web.responsebody.CursorBasedPaginationWrapper;
import com.belieme.apiserver.web.responsebody.HistoryResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
public class HistoryApiController extends BaseApiController {

  private final HistoryService historyService;

  public HistoryApiController(HistoryService historyService) {
    this.historyService = historyService;
  }

  @GetMapping("/${api.keyword.history}")
  public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfDepartment(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestParam(value = "${api.query.department-id}") String departmentId,
      @RequestParam(value = "${api.query.requester-id}", required = false) String requesterId,
      @RequestParam(value = "status", required = false) String statusStr) {
    HistoryStatus status = parseHistoryStatus(statusStr);
    return getListByDepartment(userToken, departmentId, requesterId, status);
  }

  @GetMapping("/${api.keyword.history}/with-pagination")
  public ResponseEntity<CursorBasedPaginationWrapper<HistoryResponse>> getAllHistoriesOfDepartment(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestParam(value = "${api.query.department-id}") String departmentId,
      @RequestParam(value = "${api.query.requester-id}", required = false) String requesterId,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "cursor", required = false) String cursor,
      @RequestParam(value = "limit", required = false) Integer limit) {
    int defaultLimit = 10;
    if (limit != null) {
      defaultLimit = limit;
    }
    return getListByDepartmentAndStatusWithCursor(userToken, departmentId,
        requesterId, status, cursor, defaultLimit);
  }

  @GetMapping("/${api.keyword.history}/${api.keyword.history-index}")
  public ResponseEntity<HistoryResponse> getHistory(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params) {
    UUID historyId = toUUID(params.get(api.getVariable().historyIndex()));

    HistoryDto historyDto = historyService.getById(userToken, historyId);
    HistoryResponse response = HistoryResponse.from(historyDto);
    return ResponseEntity.ok(response);
  }

  private ResponseEntity<List<HistoryResponse>> getListByDepartment(
      String userToken, String departmentId, String requesterId,
      HistoryStatus status) {
    if (requesterId == null) {
      List<HistoryDto> historyDtoList = historyService.getListByDepartment(
          userToken, toUUID(departmentId), status);
      List<HistoryResponse> responseList = toResponseList(historyDtoList);
      return ResponseEntity.ok(responseList);
    }

    List<HistoryDto> historyDtoList = historyService.getListByDepartmentAndRequester(
        userToken, toUUID(departmentId), toUUID(requesterId), status);
    List<HistoryResponse> responseList = toResponseList(historyDtoList);
    return ResponseEntity.ok(responseList);
  }

  private ResponseEntity<CursorBasedPaginationWrapper<HistoryResponse>> getListByDepartmentAndStatusWithCursor(
      String userToken, String departmentId, String requesterId,
      String statusStr, String cursorStr, int limit) {
    HistoryStatus status = parseHistoryStatus(statusStr);
    if (requesterId == null) {
      return makePaginatedResponse(cursorStr,
          (cursor) -> historyService.getListByDepartment(userToken,
              toUUID(departmentId), status, cursor, limit));
    }
    return makePaginatedResponse(cursorStr,
        (cursor) -> historyService.getListByDepartmentAndRequester(userToken,
            toUUID(departmentId), toUUID(requesterId), status, cursor, limit));
  }

  private HistoryStatus parseHistoryStatus(String statusStr) {
    if (statusStr == null) {
      return null;
    }
    try {
      return HistoryStatus.valueOf(statusStr);
    } catch (Exception e) {
      throw new BadRequestException();
    }
  }

  private List<HistoryResponse> toResponseList(
      List<HistoryDto> historyDtoList) {
    List<HistoryResponse> responseList = new ArrayList<>();
    for (HistoryDto dto : historyDtoList) {
      responseList.add(HistoryResponse.from(dto));
    }
    return responseList;
  }

  private ResponseEntity<CursorBasedPaginationWrapper<HistoryResponse>> makePaginatedResponse(
      String curCursorStr, MethodToGetPaginatedList method) {
    List<HistoryDto> historyDtoList = method.method(curCursorStr);
    HistoryCursorDto nextCursor = getNextCursor(historyDtoList);
    if (nextCursor != null) {
      List<HistoryDto> nextHistoryDtoList = method.method(
          nextCursor.getCursorString());
      if (nextHistoryDtoList.size() == 0) {
        nextCursor = null;
      }
    }
    return makeCursorResponse(historyDtoList, nextCursor);
  }

  private HistoryCursorDto getNextCursor(List<HistoryDto> historyDtoList) {
    if (historyDtoList.size() == 0) {
      return null;
    }
    return HistoryCursorDto.nextCursor(
        historyDtoList.get(historyDtoList.size() - 1));
  }

  private ResponseEntity<CursorBasedPaginationWrapper<HistoryResponse>> makeCursorResponse(
      List<HistoryDto> historyDtoList, HistoryCursorDto nextCursor) {
    List<HistoryResponse> responseList = toResponseList(historyDtoList);
    if (nextCursor == null) {
      return ResponseEntity.ok(
          new CursorBasedPaginationWrapper<>(responseList, responseList.size(),
              null));
    }
    return ResponseEntity.ok(
        new CursorBasedPaginationWrapper<>(responseList, responseList.size(),
            nextCursor.getCursorString()));
  }

  private interface MethodToGetPaginatedList {

    List<HistoryDto> method(String cursorStr);
  }
}
