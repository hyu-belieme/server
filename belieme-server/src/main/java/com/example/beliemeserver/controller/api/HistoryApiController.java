package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.BadRequestHttpException;
import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/universities/{university-code}/departments/{department-code}")
public class HistoryApiController {
    private final HistoryService historyService;

    public HistoryApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/histories")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @RequestParam("requester-university-code") String requesterUniversityCode,
            @RequestParam("requester-student-id") String requesterStudentId
    ) {
        if(requesterUniversityCode == null && requesterStudentId == null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartment(
                    userToken, universityCode, departmentCode);
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        if(requesterUniversityCode != null && requesterStudentId != null) {
            List<HistoryDto> historyDtoList = historyService.getListByDepartmentAndRequester(
                    userToken, universityCode, departmentCode,
                    requesterUniversityCode, requesterStudentId
            );
            List<HistoryResponse> responseList = toResponseList(historyDtoList);
            return ResponseEntity.ok(responseList);
        }

        // TODO exception 한번에 정리할 때 손 보기
        throw new BadRequestHttpException("");
    }

    @GetMapping("/stuffs/{stuff-name}/histories")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfStuff(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName
    ) {
        List<HistoryDto> historyDtoList = historyService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/stuffs/{stuff-name}/items/{item-num}/histories")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfItem(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName,
            @PathVariable("item-num") int itemNum
    ) {
        List<HistoryDto> historyDtoList = historyService.getListByItem(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    private List<HistoryResponse> toResponseList(List<HistoryDto> historyDtoList) {
        List<HistoryResponse> responseList = new ArrayList<>();
        for(HistoryDto dto : historyDtoList) {
            responseList.add(HistoryResponse.from(dto));
        }
        return responseList;
    }
}
