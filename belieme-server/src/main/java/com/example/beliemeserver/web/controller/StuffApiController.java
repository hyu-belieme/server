package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.service._new.NewStuffService;
import com.example.beliemeserver.web.requestbody._new.StuffRequest;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.StuffUpdateValidationGroup;
import com.example.beliemeserver.web.responsebody._new.StuffResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}")
public class StuffApiController extends BaseApiController {
    private final NewStuffService stuffService;

    public StuffApiController(NewStuffService stuffService) {
        this.stuffService = stuffService;
    }

    @GetMapping("/${api.keyword.stuff}")
    public ResponseEntity<List<StuffResponse>> getAllStuffListOfDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        List<StuffDto> stuffDtoList = stuffService.getListByDepartment(
                userToken, universityName, departmentCode);
        List<StuffResponse> responseList = toResponseList(stuffDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.stuff}/${api.keyword.stuff-index}")
    public ResponseEntity<StuffResponse> getStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        StuffDto stuffDto = stuffService.getByIndex(
                userToken, universityName, departmentCode, stuffName);
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.stuff}")
    public ResponseEntity<StuffResponse> createNewStuff(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Validated(StuffCreateValidationGroup.class) StuffRequest newStuff
    ) {
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        StuffDto stuffDto = stuffService.create(
                userToken, universityName, departmentCode,
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
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());
        String stuffName = params.get(api.variable().stuffIndex());

        StuffDto stuffDto = stuffService.update(
                userToken, universityName, departmentCode, stuffName,
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
}
