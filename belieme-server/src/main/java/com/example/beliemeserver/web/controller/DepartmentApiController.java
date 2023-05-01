package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.service._new.NewDepartmentService;
import com.example.beliemeserver.web.requestbody._new.DepartmentRequest;
import com.example.beliemeserver.web.requestbody.validatemarker.DepartmentCreateValidationGroup;
import com.example.beliemeserver.web.requestbody.validatemarker.DepartmentUpdateValidationGroup;
import com.example.beliemeserver.web.responsebody._new.DepartmentResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "")
public class DepartmentApiController extends BaseApiController {
    private final NewDepartmentService departmentService;

    public DepartmentApiController(NewDepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/${api.keyword.department}")
    public ResponseEntity<List<DepartmentResponse>> getAccessibleDepartmentList(
            @RequestHeader("${api.header.user-token}") String userToken
    ) {
        List<DepartmentDto> departmentDtoList = departmentService.getAccessibleList(userToken);
        List<DepartmentResponse> responseList = toResponseList(departmentDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}")
    public ResponseEntity<DepartmentResponse> getDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        DepartmentDto departmentDto = departmentService.getByIndex(userToken, universityName, departmentCode);
        DepartmentResponse response = DepartmentResponse.from(departmentDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}")
    public ResponseEntity<DepartmentResponse> createNewDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Validated(DepartmentCreateValidationGroup.class) DepartmentRequest newDepartment
    ) {
        String universityName = params.get(api.variable().universityIndex());

        DepartmentDto newDepartmentDto = departmentService.create(
                userToken, universityName, newDepartment.getName(), newDepartment.getBaseMajors()
        );
        DepartmentResponse response = DepartmentResponse.from(newDepartmentDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Validated(DepartmentUpdateValidationGroup.class) DepartmentRequest newDepartment
    ) {
        String universityName = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        DepartmentDto newDepartmentDto = departmentService.update(
                userToken, universityName, departmentCode,
                newDepartment.getName(), newDepartment.getBaseMajors()
        );
        DepartmentResponse response = DepartmentResponse.from(newDepartmentDto);
        return ResponseEntity.ok(response);
    }

    private List<DepartmentResponse> toResponseList(List<DepartmentDto> departmentDtoList) {
        List<DepartmentResponse> responseList = new ArrayList<>();
        for (DepartmentDto dto : departmentDtoList) {
            responseList.add(DepartmentResponse.from(dto));
        }
        return responseList;
    }
}
