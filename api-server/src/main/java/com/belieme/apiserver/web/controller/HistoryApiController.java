package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.service.HistoryService;
import com.belieme.apiserver.error.exception.BadRequestException;
import com.belieme.apiserver.web.responsebody.HistoryResponse;
import com.belieme.apiserver.domain.dto.HistoryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            @RequestParam(value = "${api.query.department-id}", required = false) String departmentId,
            @RequestParam(value = "${api.query.requester-id}", required = false) String requesterId,
            @RequestParam(value = "${api.query.stuff-id}", required = false) String stuffId,
            @RequestParam(value = "${api.query.item-id}", required = false) String itemId
    ) {
        if (departmentId != null && stuffId == null && itemId == null) {
            return getListByDepartment(userToken, departmentId, requesterId);
        }
        if (stuffId != null && departmentId == null && requesterId == null && itemId == null) {
            return getListByStuff(userToken, stuffId);
        }
        if (itemId != null && departmentId == null && requesterId == null && stuffId == null) {
            return getListByItem(userToken, itemId);
        }

        throw new BadRequestException();
    }

    @GetMapping("/${api.keyword.history}/${api.keyword.history-index}")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID historyId = toUUID(params.get(api.getVariable().historyIndex()));

        HistoryDto historyDto = historyService.getById(userToken, historyId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<List<HistoryResponse>> getListByDepartment(String userToken, String departmentId, String requesterId) {
        if (requesterId == null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartment(userToken, toUUID(departmentId));
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        List<HistoryDto> historyDtoList = historyService.getListByDepartmentAndRequester(
                userToken, toUUID(departmentId), toUUID(requesterId));
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    private ResponseEntity<List<HistoryResponse>> getListByStuff(String userToken, String stuffId) {
        List<HistoryDto> historyDtoList = historyService.getListByStuff(
                userToken, toUUID(stuffId));
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    private ResponseEntity<List<HistoryResponse>> getListByItem(String userToken, String itemId) {
        List<HistoryDto> historyDtoList = historyService.getListByItem(
                userToken, toUUID(itemId));
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    private List<HistoryResponse> toResponseList(List<HistoryDto> historyDtoList) {
        List<HistoryResponse> responseList = new ArrayList<>();
        for (HistoryDto dto : historyDtoList) {
            responseList.add(HistoryResponse.from(dto));
        }
        return responseList;
    }
}
