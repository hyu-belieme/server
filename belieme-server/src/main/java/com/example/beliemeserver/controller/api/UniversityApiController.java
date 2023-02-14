package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.UniversityResponse;
import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.service.UniversityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="/universities")
public class UniversityApiController {
    private final UniversityService universityService;


    public UniversityApiController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping("")
    public ResponseEntity<List<UniversityResponse>> getAllUniversities(
            @RequestHeader("user-token") String userToken
    ) {
        List<UniversityDto> universityDtoList = universityService.getAllList(userToken);
        List<UniversityResponse> responseList = toResponseList(universityDtoList);

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{university-code}")
    public ResponseEntity<UniversityResponse> getUniversity(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode
    ) {
        UniversityDto universityDto = universityService.getByIndex(userToken, universityCode);
        UniversityResponse response = UniversityResponse.from(universityDto);
        return ResponseEntity.ok(response);
    }

    private List<UniversityResponse> toResponseList(List<UniversityDto> universityDtoList) {
        List<UniversityResponse> responseList = new ArrayList<>();
        for(UniversityDto dto : universityDtoList) {
            responseList.add(UniversityResponse.from(dto));
        }
        return responseList;
    }
}
