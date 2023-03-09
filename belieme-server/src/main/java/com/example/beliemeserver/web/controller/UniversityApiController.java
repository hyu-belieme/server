package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.web.responsebody.UniversityResponse;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/${api.university}")
public class UniversityApiController extends BaseApiController {
    private final UniversityService universityService;

    public UniversityApiController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping("")
    public ResponseEntity<List<UniversityResponse>> getAllUniversities(
            @RequestHeader("${header.userToken}") String userToken
    ) {
        List<UniversityDto> universityDtoList = universityService.getAllList(userToken);
        List<UniversityResponse> responseList = toResponseList(universityDtoList);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/${api.universityIndex}")
    public ResponseEntity<UniversityResponse> getUniversity(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);

        UniversityDto universityDto = universityService.getByIndex(userToken, universityCode);
        UniversityResponse response = UniversityResponse.from(universityDto);
        return ResponseEntity.ok(response);
    }

    private List<UniversityResponse> toResponseList(List<UniversityDto> universityDtoList) {
        List<UniversityResponse> responseList = new ArrayList<>();
        for (UniversityDto dto : universityDtoList) {
            responseList.add(UniversityResponse.from(dto));
        }
        return responseList;
    }
}
