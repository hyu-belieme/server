package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.requestbody.DepartmentRequest;
import com.example.beliemeserver.controller.responsebody.DepartmentResponse;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.service.DepartmentService;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@PropertySource("classpath:urls/url.properties")
@RequestMapping(path = "")
public class DepartmentApiController extends BaseApiController {
    private final DepartmentService departmentService;

    public DepartmentApiController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/${api.department}")
    public ResponseEntity<List<DepartmentResponse>> getAccessibleDepartmentList(
            @RequestHeader("${header.userToken}") String userToken
    ) {
        List<DepartmentDto> departmentDtoList = departmentService.getAccessibleList(userToken);
        List<DepartmentResponse> responseList = toResponseList(departmentDtoList);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}")
    public ResponseEntity<DepartmentResponse> getDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

        DepartmentDto departmentDto = departmentService.getByIndex(userToken, universityCode, departmentCode);
        DepartmentResponse response = DepartmentResponse.from(departmentDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/${api.university}/${api.universityIndex}/${api.department}")
    public ResponseEntity<DepartmentResponse> createNewDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Valid DepartmentRequest newDepartment
    ) {
        String universityCode = params.get(universityIndexTag);

        DepartmentDto newDepartmentDto = departmentService.create(
                userToken, universityCode, newDepartment.getCode(),
                newDepartment.getName(), newDepartment.getBaseMajors()
        );
        DepartmentResponse response = DepartmentResponse.from(newDepartmentDto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}")
    public ResponseEntity<DepartmentResponse> updateDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params,
            @RequestBody @Valid DepartmentRequest newDepartment
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

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
