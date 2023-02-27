package com.example.beliemeserver.controller.api.old;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.old.OldLoginInfoRequest;
import com.example.beliemeserver.controller.responsebody.old.OldUserResponse;

import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.service.old.OldUserService;
import com.example.beliemeserver.model.dto.old.OldUserDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/old")
public class OldUserApiController {
    private final OldUserService userService;

    public OldUserApiController(OldUserService userService) {
        this.userService = userService;
    }

    @PatchMapping("login/")
    public ResponseEntity<OldUserResponse> getUserInfoFromUnivApi(@RequestBody OldLoginInfoRequest requestBody) throws ConflictHttpException, NotFoundHttpException, ForbiddenHttpException, BadRequestHttpException, InternalServerErrorHttpException {
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

        return ResponseEntity.ok(OldUserResponse.from(savedUserDto));
    }

    @GetMapping("auth/")
    public ResponseEntity<OldUserResponse> getUserUsingUserToken(@RequestHeader(value = "User-Token") String userToken) throws UnauthorizedHttpException, InternalServerErrorHttpException {
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
        return ResponseEntity.ok().body(OldUserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_staff")
    public ResponseEntity<OldUserResponse> makePermissionStaff(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
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
        return ResponseEntity.ok().body(OldUserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_master")
    public ResponseEntity<OldUserResponse> makePermissionMaster(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
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
        return ResponseEntity.ok().body(OldUserResponse.from(target));
    }

    @PatchMapping("users/{studentId}/make_user")
    public ResponseEntity<OldUserResponse> makePermissionUser(@RequestHeader(value = "User-Token") String userToken, @PathVariable String studentId) throws InternalServerErrorHttpException, ForbiddenHttpException, ConflictHttpException, UnauthorizedHttpException, NotFoundHttpException, MethodNotAllowedHttpException {
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
        return ResponseEntity.ok().body(OldUserResponse.from(target));
    }
}
