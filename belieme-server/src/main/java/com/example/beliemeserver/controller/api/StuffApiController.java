package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.requestbody.StuffRequest;
import com.example.beliemeserver.controller.responsebody.StuffResponse;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.service.StuffService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}")
public class StuffApiController extends BaseApiController {
    private final StuffService stuffService;

    public StuffApiController(StuffService stuffService) {
        this.stuffService = stuffService;
    }

    @GetMapping("/${api.stuff}")
    public ResponseEntity<List<StuffResponse>> getAllStuffListOfDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

        List<StuffDto> stuffDtoList = stuffService.getListByDepartment(
                userToken, universityCode, departmentCode);
        List<StuffResponse> responseList = toResponseList(stuffDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.stuff}/${api.stuffIndex}")
    public ResponseEntity<StuffResponse> getStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

        StuffDto stuffDto = stuffService.getByIndex(
                userToken, universityCode, departmentCode, stuffName);
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.stuff}")
    public ResponseEntity<StuffResponse> createNewStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Valid StuffRequest newStuff
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

        StuffDto stuffDto = stuffService.create(
                userToken, universityCode, departmentCode,
                newStuff.getName(), newStuff.getEmoji(), newStuff.getAmount());
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.stuff}/${api.stuffIndex}")
    public ResponseEntity<StuffResponse> updateStuff(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Valid StuffRequest newStuff
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);
        String stuffName = params.get(stuffIndexTag);

        StuffDto stuffDto = stuffService.update(
                userToken, universityCode, departmentCode, stuffName,
                newStuff.getName(), newStuff.getEmoji());
        StuffResponse response = StuffResponse.from(stuffDto);
        return ResponseEntity.ok(response);
    }

    private List<StuffResponse> toResponseList(List<StuffDto> stuffDtoList) {
        List<StuffResponse> responseList = new ArrayList<>();
        for (StuffDto dto : stuffDtoList) {
            responseList.add(StuffResponse.from(dto));
        }
        return responseList;
    }
}
