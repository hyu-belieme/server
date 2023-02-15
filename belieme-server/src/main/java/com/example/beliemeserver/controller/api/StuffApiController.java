package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.StuffResponse;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.service.StuffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/universities/{university-code}/departments/{department-code}")
public class StuffApiController {
    private final StuffService stuffService;

    public StuffApiController(StuffService stuffService) {
        this.stuffService = stuffService;
    }

    @GetMapping("/stuffs")
    public ResponseEntity<List<StuffResponse>> getAllStuffListOfDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode
    ) {
        List<StuffDto> stuffDtoList = stuffService.getListByDepartment(
                userToken, universityCode, departmentCode);
        List<StuffResponse> responseList = toResponseList(stuffDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/stuffs/{stuff-name}")
    public ResponseEntity<StuffResponse> getStuff(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @PathVariable("stuff-name") String stuffName
    ) {
        StuffDto stuffDto = stuffService.getByIndex(
                userToken, universityCode, departmentCode,stuffName);
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    private List<StuffResponse> toResponseList(List<StuffDto> stuffDtoList) {
        List<StuffResponse> responseList = new ArrayList<>();
        for(StuffDto dto : stuffDtoList) {
            responseList.add(StuffResponse.from(dto));
        }
        return responseList;
    }
}
