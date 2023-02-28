package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.requestbody.DepartmentRequest;
import com.example.beliemeserver.controller.responsebody.DepartmentResponse;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "")
public class DepartmentApiController {
    private final DepartmentService departmentService;

    public DepartmentApiController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentResponse>> getAccessibleDepartmentList(
            @RequestHeader("user-token") String userToken
    ) {
        List<DepartmentDto> departmentDtoList = departmentService.getAccessibleList(userToken);
        List<DepartmentResponse> responseList = toResponseList(departmentDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/universities/{university-code}/departments/{department-code}")
    public ResponseEntity<DepartmentResponse> getDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode
    ) {
        DepartmentDto departmentDto = departmentService.getByIndex(userToken, universityCode, departmentCode);
        DepartmentResponse response = DepartmentResponse.from(departmentDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/universities/{university-code}/departments")
    public ResponseEntity<DepartmentResponse> createNewDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @RequestBody DepartmentRequest newDepartment
    ) {
        DepartmentDto newDepartmentDto = departmentService.create(
                userToken, universityCode, newDepartment.getCode(),
                newDepartment.getName(), newDepartment.getBaseMajors()
        );
        DepartmentResponse response = DepartmentResponse.from(newDepartmentDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/universities/{university-code}/departments/{department-code}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode,
            @RequestBody DepartmentRequest newDepartment
    ) {
        DepartmentDto newDepartmentDto = departmentService.update(
                userToken, universityCode, departmentCode,
                newDepartment.getCode(), newDepartment.getName(),
                newDepartment.getBaseMajors()
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
