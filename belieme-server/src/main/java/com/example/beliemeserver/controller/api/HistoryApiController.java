package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.HistoryResponse;
import com.example.beliemeserver.exception.BadRequestException;
import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}")
public class HistoryApiController extends BaseApiController {
    private final HistoryService historyService;

    public HistoryApiController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/${api.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestParam(value = "${requestQuery.requester.univCode}", required = false) String requesterUniversityCode,
            @RequestParam(value = "${requestQuery.requester.studentId}", required = false) String requesterStudentId
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

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

    @GetMapping("/${api.stuff}/${api.stuffIndex}/${api.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

        List<HistoryDto> historyDtoList = historyService.getListByStuff(
                userToken, universityCode, departmentCode, stuffName);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.history}")
    public ResponseEntity<List<HistoryResponse>> getAllHistoriesOfItem(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        List<HistoryDto> historyDtoList = historyService.getListByItem(
                userToken, universityCode, departmentCode, stuffName, itemNum);
        List<HistoryResponse> responseList = toResponseList(historyDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.history}/${api.historyIndex}")
    public ResponseEntity<HistoryResponse> getHistory(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));
        int historyNum = Integer.parseInt(params.get(historyIndexTag));

        HistoryDto historyDto = historyService.getByIndex(
                userToken, universityCode, departmentCode,
                stuffName, itemNum, historyNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.reserve}")
    public ResponseEntity<HistoryResponse> reserveStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

        HistoryDto historyDto = historyService.createReservation(
                userToken, universityCode, departmentCode,
                stuffName, null);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.reserve}")
    public ResponseEntity<HistoryResponse> reserveItem(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        HistoryDto historyDto = historyService.createReservation(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.lost}")
    public ResponseEntity<HistoryResponse> makeItemLost(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        HistoryDto historyDto = historyService.makeItemLost(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.approve}")
    public ResponseEntity<HistoryResponse> makeItemApprove(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        HistoryDto historyDto = historyService.makeItemUsing(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.return}")
    public ResponseEntity<HistoryResponse> makeItemReturn(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

        HistoryDto historyDto = historyService.makeItemReturn(
                userToken, universityCode, departmentCode,
                stuffName, itemNum);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}/${api.item}/${api.itemIndex}/${api.cancel}")
    public ResponseEntity<HistoryResponse> makeItemCancel(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);
        int itemNum = Integer.parseInt(params.get(itemIndexTag));

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
