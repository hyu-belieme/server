package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.LoginInfoRequest;
import com.example.beliemeserver.controller.responsebody.UserResponse;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.service.UserService;
import com.example.beliemeserver.model.dto.old.OldUserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserApiController {
    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("login/")
    public ResponseEntity<UserResponse> getUserInfoFromUnivApi(@RequestBody LoginInfoRequest requestBody) throws ConflictHttpException, NotFoundHttpException, ForbiddenHttpException, BadRequestHttpException, InternalServerErrorHttpException {
        if(requestBody == null || requestBody.getApiToken() == null) {
            throw new BadRequestHttpException("Request body에 정보가 부족합니다. 필요한 정보 : apiToken(String)");
        }

        OldUserDto savedUserDto;
        try {
            savedUserDto = userService.getUserInfoFromUnivApiByApiToken(requestBody.getApiToken());
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (BadGateWayException e) {
            e.printStackTrace();
            throw new BadRequestHttpException(e);
        }

        return ResponseEntity.ok(UserResponse.from(savedUserDto));
    }

    @GetMapping("auth/")
    public ResponseEntity<UserResponse> getUserUsingUserToken(@RequestHeader(value = "User-Token") String userToken) throws UnauthorizedHttpException, InternalServerErrorHttpException {
        OldUserDto target;
        try {
            target = userService.getUserByUserToken(userToken);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        }
        return ResponseEntity.ok().body(UserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_staff")
    public ResponseEntity<UserResponse> makePermissionStaff(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
        OldUserDto target;
        try {
            target = userService.makeUserHavePermission(userToken, studentId, OldAuthorityDto.Permission.STAFF);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok().body(UserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_master")
    public ResponseEntity<UserResponse> makePermissionMaster(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
        OldUserDto target;
        try {
            target = userService.makeUserHavePermission(userToken, studentId, OldAuthorityDto.Permission.MASTER);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok().body(UserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_user")
    public ResponseEntity<UserResponse> makePermissionUser(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
        OldUserDto target;
        try {
            target = userService.makeUserHavePermission(userToken, studentId, OldAuthorityDto.Permission.USER);
        } catch (DataException e) {
            e.printStackTrace();
            throw new InternalServerErrorHttpException(e);
        } catch (ForbiddenException e) {
            e.printStackTrace();
            throw new ForbiddenHttpException(e);
        } catch (ConflictException e) {
            e.printStackTrace();
            throw new ConflictHttpException(e);
        } catch (UnauthorizedException e) {
            e.printStackTrace();
            throw new UnauthorizedHttpException(e);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new NotFoundHttpException(e);
        } catch (MethodNotAllowedException e) {
            e.printStackTrace();
            throw new MethodNotAllowedHttpException(e);
        }
        return ResponseEntity.ok().body(UserResponse.from(target));
    }
}
