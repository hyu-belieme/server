package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.dto.UniversityDto;
import com.belieme.apiserver.domain.service.UniversityService;
import com.belieme.apiserver.web.responsebody.UniversityResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/${api.keyword.university}")
public class UniversityApiController extends BaseApiController {

  private final UniversityService universityService;

  public UniversityApiController(UniversityService universityService) {
    this.universityService = universityService;
  }

  @GetMapping("")
  public ResponseEntity<List<UniversityResponse>> getAllUniversities(
      @RequestHeader("${api.header.user-token}") String userToken) {
    List<UniversityDto> universityDtoList = universityService.getAllList(userToken);
    List<UniversityResponse> responseList = toResponseList(universityDtoList);

    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/${api.keyword.university-index}")
  public ResponseEntity<UniversityResponse> getUniversity(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params) {
    String universityId = params.get(api.getVariable().universityIndex());

    UniversityDto universityDto = universityService.getById(userToken, toUUID(universityId));
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
