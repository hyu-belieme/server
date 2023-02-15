package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.responsebody.UserResponse;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.model.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path="")
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/my")
    public ResponseEntity<UserResponse> getMyUserInfo(
            @RequestHeader("user-token") String userToken
    ) {
        UserDto userDto = userService.getByToken(userToken);
        UserResponse response = UserResponse.from(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/universities/{university-code}/users/{student-id}")
    public ResponseEntity<UserResponse> getUserInfo(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("student-id") String studentId
    ) {
        UserDto userDto = userService.getByIndex(userToken, universityCode, studentId);
        UserResponse response = UserResponse.from(userDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/universities/{university-code}/departments/{department-code}/users")
    public ResponseEntity<List<UserResponse>> getUserListOfDepartment(
            @RequestHeader("user-token") String userToken,
            @PathVariable("university-code") String universityCode,
            @PathVariable("department-code") String departmentCode
    ) {
        List<UserDto> userDtoList = userService.getListByDepartment(userToken, universityCode, departmentCode);
        List<UserResponse> responseList = toResponseList(userDtoList);
        return ResponseEntity.ok(responseList);
    }

    private List<UserResponse> toResponseList(List<UserDto> userDtoList) {
        List<UserResponse> responseList = new ArrayList<>();
        for(UserDto dto : userDtoList) {
            responseList.add(UserResponse.from(dto));
        }
        return responseList;
    }
}
