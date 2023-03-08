package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.controller.responsebody.UserResponse;
import com.example.beliemeserver.exception.BadRequestException;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "")
public class UserApiController extends BaseApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/${api.myInfo}")
    public ResponseEntity<UserResponse> getMyUserInfo(
            @RequestHeader("${header.userToken}") String userToken
    ) {
        UserDto userDto = userService.getByToken(userToken);
        UserResponse response = UserResponse.from(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/${api.university}/${api.universityIndex}/${api.user}/${api.userIndex}")
    public ResponseEntity<UserResponse> getUserInfo(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String studentId = params.get(userIndexTag);

        UserDto userDto = userService.getByIndex(userToken, universityCode, studentId);
        UserResponse response = UserResponse.from(userDto).withoutSecureInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/${api.university}/${api.universityIndex}/${api.department}/${api.departmentIndex}/${api.user}")
    public ResponseEntity<List<UserResponse>> getUserListOfDepartment(
            @RequestHeader("${header.userToken}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);
        String departmentCode = params.get(departmentIndexTag);

        List<UserDto> userDtoList = userService.getListByDepartment(userToken, universityCode, departmentCode);
        List<UserResponse> responseList = toResponseWithoutSecureInfoList(userDtoList);
        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/${api.university}/${api.universityIndex}/${api.loginExternalApi}")
    public ResponseEntity<UserResponse> loginByUniversityApi(
            @RequestHeader("${header.externalApiToken}") String apiToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(universityIndexTag);

        if (universityCode.equals(Globals.DEV_UNIVERSITY.code())) {
            UserDto userDto = userService.reloadDeveloperUser(apiToken);
            UserResponse response = UserResponse.from(userDto);
            return ResponseEntity.ok(response);
        }
        if (universityCode.equals(Globals.HANYANG_UNIVERSITY.code())) {
            UserDto userDto = userService.reloadHanyangUniversityUser(apiToken);
            UserResponse response = UserResponse.from(userDto);
            return ResponseEntity.ok(response);
        }

        throw new BadRequestException();
    }

    private List<UserResponse> toResponseWithoutSecureInfoList(List<UserDto> userDtoList) {
        List<UserResponse> responseList = new ArrayList<>();
        for (UserDto dto : userDtoList) {
            responseList.add(UserResponse.from(dto).withoutSecureInfo());
        }
        return responseList;
    }
}
