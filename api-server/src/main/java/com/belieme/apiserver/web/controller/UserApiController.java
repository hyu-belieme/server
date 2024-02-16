package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.domain.dto.UserDto;
import com.belieme.apiserver.domain.dto.enumeration.Permission;
import com.belieme.apiserver.domain.service.UserService;
import com.belieme.apiserver.error.exception.BadRequestException;
import com.belieme.apiserver.error.exception.ServerException;
import com.belieme.apiserver.error.exception.UnauthorizedException;
import com.belieme.apiserver.web.requestbody.PermissionRequest;
import com.belieme.apiserver.web.responsebody.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "")
@Validated
public class UserApiController extends BaseApiController {

  private final UserService userService;

  public UserApiController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/${api.keyword.user}")
  public ResponseEntity<List<UserResponse>> getUserListOfDepartment(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestParam(value = "${api.query.department-id}", required = false) String departmentId) {
    if (departmentId == null) {
      List<UserDto> userDtoList = userService.getAllList(userToken);
      List<UserResponse> responseList = toResponseWithoutSecureInfoList(userDtoList);
      return ResponseEntity.ok(responseList);
    }

    List<UserDto> userDtoList = userService.getListByDepartment(userToken, toUUID(departmentId));
    List<UserResponse> responseList = toResponseWithoutSecureInfoList(userDtoList);
    return ResponseEntity.ok(responseList);
  }

  @GetMapping("/${api.keyword.user}/${api.keyword.user-index}")
  public ResponseEntity<UserResponse> getUserInfo(
      @RequestHeader("${api.header.user-token}") String userToken,
      @PathVariable Map<String, String> params) {
    UUID userId = toUUID(params.get(api.getVariable().userIndex()));

    UserDto userDto = userService.getById(userToken, userId);
    UserResponse response = UserResponse.from(userDto).withoutSecureInfo();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/${api.keyword.user}/${api.keyword.by-index}")
  public ResponseEntity<UserResponse> getUserInfoByUnivIdAndStudentId(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestParam (value = "${api.query.university-id}", required = true) String universityId,
      @RequestParam (value = "${api.query.student-id}", required = true) String studentId
  ) {
    UUID parsedUniversityId = toUUID(universityId);

    UserDto userDto = userService.getByUnivIdAndStudentId(userToken, parsedUniversityId, studentId);
    UserResponse response = UserResponse.from(userDto).withoutSecureInfo();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/${api.keyword.my-info}")
  public ResponseEntity<UserResponse> getMyUserInfo(
      @RequestHeader("${api.header.user-token}") String userToken) {
    UserDto userDto = userService.getByToken(userToken);
    UserResponse response = UserResponse.from(userDto).withoutSecureInfo();
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/${api.keyword.user}/${api.keyword.update-permission}")
  public ResponseEntity<List<UserResponse>> updatePermissionOfUser(
      @RequestHeader("${api.header.user-token}") String userToken,
      @RequestBody @Size(max=200) List<@Valid PermissionRequest> permissionRequests) {
    List<UserDto> results = new ArrayList<>();
    for(PermissionRequest permissionRequest : permissionRequests) {
      try {
        UUID userId = toUUID(permissionRequest.getUserId());
        UUID parsedDeptId = toUUID(permissionRequest.getDepartmentId());

        results.add(
            userService.updateAuthorityOfUser(userToken, userId, parsedDeptId,
                Permission.valueOf(permissionRequest.getPermission())));
      } catch (ServerException | IllegalArgumentException ignored) {
      }
    }
    List<UserResponse> response = results.stream().map(e -> UserResponse.from(e).withoutSecureInfo()).toList();
    return ResponseEntity.ok(response);
  }

  @PatchMapping("/${api.keyword.university}/${api.keyword.university-index}/${api.keyword.login-external-api}")
  public ResponseEntity<UserResponse> loginByUniversityApi(
      @RequestHeader("${api.header.external-api-token}") String apiToken,
      @PathVariable Map<String, String> params) {
    UUID universityId = toUUID(params.get(api.getVariable().universityIndex()));

    UserDto userDto = userService.reloadInitialUser(universityId, apiToken);
    if (userDto != null) {
      UserResponse response = UserResponse.from(userDto);
      return ResponseEntity.ok(response);
    }

    if (universityId.equals(userService.getDeveloperUniversityId())) {
      throw new UnauthorizedException();
    }
    if (universityId.equals(userService.getHanyangUniversityId())) {
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
