package com.example.beliemeserver.web.controller;

import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.domain.service.UserService;
import com.example.beliemeserver.error.exception.BadRequestException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import com.example.beliemeserver.web.responsebody.UserResponse;
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

    @GetMapping("/${api.keyword.my-info}")
    public ResponseEntity<UserResponse> getMyUserInfo(
            @RequestHeader("${api.header.user-token}") String userToken
    ) {
        UserDto userDto = userService.getByToken(userToken);
        UserResponse response = UserResponse.from(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.user}/${api.keyword.user-index}")
    public ResponseEntity<UserResponse> getUserInfo(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String studentId = params.get(api.variable().userIndex());

        UserDto userDto = userService.getByIndex(userToken, universityCode, studentId);
        UserResponse response = UserResponse.from(userDto).withoutSecureInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.department}/${api.keyword.department-index}/${api.keyword.user}")
    public ResponseEntity<List<UserResponse>> getUserListOfDepartment(
            @RequestHeader("${api.header.user-token}") String userToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());
        String departmentCode = params.get(api.variable().departmentIndex());

        List<UserDto> userDtoList = userService.getListByDepartment(userToken, universityCode, departmentCode);
        List<UserResponse> responseList = toResponseWithoutSecureInfoList(userDtoList);
        return ResponseEntity.ok(responseList);
    }

    @PatchMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.login-external-api}")
    public ResponseEntity<UserResponse> loginByUniversityApi(
            @RequestHeader("${api.header.external-api-token}") String apiToken,
            @PathVariable Map<String, String> params
    ) {
        String universityCode = params.get(api.variable().universityIndex());

        UserDto userDto = userService.reloadInitialUser(universityCode, apiToken);
        if (userDto != null) {
            UserResponse response = UserResponse.from(userDto);
            return ResponseEntity.ok(response);
        }

        if (universityCode.equals(userService.getDeveloperUniversityCode())) {
            throw new UnauthorizedException();
        }
        if (universityCode.equals(userService.getHanyangUniversityCode())) {
            userDto = userService.reloadHanyangUniversityUser(apiToken);
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
