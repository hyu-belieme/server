package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.HistoryDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.service.HistoryService;
import com.example.beliemeserver.domain.service.StuffService;
import com.example.beliemeserver.web.requestbody.StuffRequest;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import com.example.beliemeserver.web.responsebody.HistoryResponse;
import com.example.beliemeserver.web.responsebody.StuffResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
            @RequestParam(value = "${api.query.department-id}") String departmentId
    ) {
        List<StuffDto> stuffDtoList = stuffService.getListByDepartment(
                userToken, toUUID(departmentId));
        List<StuffResponse> responseList = toResponseList(stuffDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}")
    public ResponseEntity<StuffResponse> getStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID stuffId = toUUID(params.get(api.variable().stuffIndex()));

        StuffDto stuffDto = stuffService.getById(userToken, stuffId);
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.stuff}")
    public ResponseEntity<StuffResponse> createNewStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestBody @Validated(StuffCreateValidationGroup.class) StuffRequest newStuff
    ) {
        System.out.println(toUUID(newStuff.getDepartmentId()));
        StuffDto stuffDto = stuffService.create(
                userToken, toUUID(newStuff.getDepartmentId()),
                newStuff.getName(), newStuff.getThumbnail(), newStuff.getAmount());
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}")
    public ResponseEntity<StuffResponse> updateStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Validated(StuffUpdateValidationGroup.class) StuffRequest newStuff
    ) {
        UUID stuffId = toUUID(params.get(api.variable().stuffIndex()));

        StuffDto stuffDto = stuffService.update(userToken, stuffId,
                newStuff.getName(), newStuff.getThumbnail());
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
            @PathVariable Map<String, String> params
    ) {
        UUID stuffId = toUUID(params.get(api.variable().stuffIndex()));

        HistoryDto historyDto = historyService.createReservationOnStuff(userToken, stuffId);
        HistoryResponse response = HistoryResponse.from(historyDto);
        return ResponseEntity.ok(response);
    }
}
