package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.HistoryDto;
import com.example.beliemeserver.domain.service.HistoryService;
import com.example.beliemeserver.error.exception.BadRequestException;
import com.example.beliemeserver.web.responsebody.HistoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}")
public class HistoryApiController extends BaseApiController {
    private final HistoryService historyService;

    public HistoryApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/${api.keyword.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestParam(value = "${api.query.requester-university-code}", required = false) String requesterUniversityCode,
            @RequestParam(value = "${api.query.requester-student-id}", required = false) String requesterStudentId
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        if (requesterUniversityCode == null && requesterStudentId == null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartment(
                    userToken, universityCode, departmentCode);
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        if (requesterUniversityCode != null && requesterStudentId != null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartmentAndRequester(
                    userToken, universityCode, departmentCode,
                    requesterUniversityCode, requesterStudentId
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
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        List<HistoryDto> historyDtoList = historyService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        List<HistoryDto> historyDtoList = historyService.getListByItem(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.history}/${api.keyword.history-index}")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));
        int historyNum = Integer.parseInt(params.get(api.variable().historyIndex()));

        HistoryDto historyDto = historyService.getByIndex(
                userToken, universityCode, departmentCode,
                stuffName, itemNum, historyNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.do-reserve}")
    public ResponseEntity<HistoryResponse> reserveStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        HistoryDto historyDto = historyService.createReservation(
                userToken, universityCode, departmentCode,
                stuffName, null);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-reserve}")
    public ResponseEntity<HistoryResponse> reserveItem(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.createReservation(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-lost}")
    public ResponseEntity<HistoryResponse> makeItemLost(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemLost(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-approve}")
    public ResponseEntity<HistoryResponse> makeItemApprove(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemUsing(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-return}")
    public ResponseEntity<HistoryResponse> makeItemReturn(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemReturn(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}/${api.keyword.item}/${api.keyword.item-index}/${api.keyword.do-cancel}")
    public ResponseEntity<HistoryResponse> makeItemCancel(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());
        int itemNum = Integer.parseInt(params.get(api.variable().itemIndex()));

        HistoryDto historyDto = historyService.makeItemCancel(
                userToken, universityCode, departmentCode,
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
