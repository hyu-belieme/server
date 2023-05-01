package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto._new.HistoryDto;
import com.example.beliemeserver.domain.service._new.NewHistoryService;
import com.example.beliemeserver.error.exception.BadRequestException;
import com.example.beliemeserver.web.responsebody._new.HistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}")
public class HistoryApiController extends BaseApiController {
    private final NewHistoryService historyService;

    public HistoryApiController(NewHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/${api.keyword.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestParam(value = "${api.query.requester-university-code}", required = false) String requesterUniversityName,
            @RequestParam(value = "${api.query.requester-student-id}", required = false) String requesterStudentId
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        if (requesterUniversityName == null && requesterStudentId == null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartment(
                    userToken, UniversityName, departmentCode);
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        if (requesterUniversityName != null && requesterStudentId != null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartmentAndRequester(
                    userToken, UniversityName, departmentCode,
                    requesterUniversityName, requesterStudentId
            );
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        throw new BadRequestException();
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        List<HistoryDto> historyDtoList = historyService.getListByStuff(
                userToken, UniversityName, departmentCode, stuffName);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        List<HistoryDto> historyDtoList = historyService.getListByItem(
                userToken, UniversityName, departmentCode, stuffName, itemNum);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.history}/${api.keyword.history-index}")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));
        int historyNum = Integer.parseInt(params.get(api.variable().historyIndex()));

        HistoryDto historyDto = historyService.getByIndex(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum, historyNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.do-reserve}")
    public ResponseEntity<HistoryResponse> reserveStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        HistoryDto historyDto = historyService.createReservation(
                userToken, UniversityName, departmentCode,
                stuffName, null);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-reserve}")
    public ResponseEntity<HistoryResponse> reserveItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.createReservation(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-lost}")
    public ResponseEntity<HistoryResponse> makeItemLost(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemLost(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-approve}")
    public ResponseEntity<HistoryResponse> makeItemApprove(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemUsing(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-return}")
    public ResponseEntity<HistoryResponse> makeItemReturn(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemReturn(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-cancel}")
    public ResponseEntity<HistoryResponse> makeItemCancel(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String UniversityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemCancel(
                userToken, UniversityName, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    private List<HistoryResponse> toResponseList(List<HistoryDto> historyDtoList) {
        List<HistoryResponse> responseList = new ArrayList<>();
        for (HistoryDto dto : historyDtoList) {
            responseList.add(HistoryResponse.from(dto));
        }
        return responseList;
    }
}
