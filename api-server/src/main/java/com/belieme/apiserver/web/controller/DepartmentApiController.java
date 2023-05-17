package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.service.DepartmentService;
import com.belieme.apiserver.web.requestbody.DepartmentRequest;
import com.belieme.apiserver.web.responsebody.DepartmentResponse;
import com.belieme.apiserver.domain.dto.DepartmentDto;
import com.belieme.apiserver.web.requestbody.validatemarker.DepartmentCreateValidationGroup;
import com.belieme.apiserver.web.requestbody.validatemarker.DepartmentUpdateValidationGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "")
public class DepartmentApiController extends BaseApiController {
    private final DepartmentService departmentService;

    public DepartmentApiController(DepartmentService departmentService) {
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

    @GetMapping("${api.keyword.department}/${api.keyword.department-index}")
    public ResponseEntity<DepartmentResponse> getDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        UUID departmentId = toUUID(params.get(api.getVariable().departmentIndex()));

        DepartmentDto departmentDto = departmentService.getById(userToken, departmentId);
        DepartmentResponse response = DepartmentResponse.from(departmentDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.keyword.department}")
    public ResponseEntity<DepartmentResponse> createNewDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @RequestBody @Validated(DepartmentCreateValidationGroup.class) DepartmentRequest newDepartment
    ) {
        UUID universityId = toUUID(newDepartment.getUniversityId());
        DepartmentDto newDepartmentDto = departmentService.create(
                userToken, universityId, newDepartment.getName(), newDepartment.getBaseMajors()
        );
        DepartmentResponse response = DepartmentResponse.from(newDepartmentDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.keyword.department}/${api.keyword.department-index}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Validated(DepartmentUpdateValidationGroup.class) DepartmentRequest newDepartment
    ) {
        UUID departmentId = toUUID(params.get(api.getVariable().departmentIndex()));

        DepartmentDto newDepartmentDto = departmentService.update(
                userToken, departmentId, newDepartment.getName(), newDepartment.getBaseMajors()
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
