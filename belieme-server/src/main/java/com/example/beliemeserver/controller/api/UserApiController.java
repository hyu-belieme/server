package com.example.beliemeserver.controller.api;

import com.example.beliemeserver.controller.httpexception.*;
import com.example.beliemeserver.controller.requestbody.LoginInfoRequest;
import com.example.beliemeserver.controller.responsebody.UserResponse;
import com.example.beliemeserver.controller.util.Globals;
import com.example.beliemeserver.controller.util.HttpRequest;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundOnDBException;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.UserDto;
import com.example.beliemeserver.service.AuthorityService;
import com.example.beliemeserver.service.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@RestController
@RequestMapping("/")
public class UserApiController {
    private static final String client_id = "a4b1abe746f384c3d43fa82a17f222";
    private static final Set<String> CSE_SOSOK_ID = new HashSet<>(Arrays.asList("FH04067"));

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @PatchMapping("login/")
    public ResponseEntity<UserResponse> getUserInfoFromUnivApi(@RequestBody LoginInfoRequest requestBody) throws BadRequestException, GateWayTimeOutException, InternalServerErrorException, ForbiddenException {
        if(requestBody == null || requestBody.getApiToken() == null) {
            throw new BadRequestException("Request body에 정보가 부족합니다. 필요한 정보 : univCode(String), apiToken(String)");
        }
        String studentId;
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "https://api.hanyang.ac.kr/");
        headers.put("client_id", client_id);
        headers.put("swap_key", Long.toString(System.currentTimeMillis()/1000));
        headers.put("access_token", requestBody.getApiToken());
        String responseString = HttpRequest.sendGetRequest("https://api.hanyang.ac.kr/rs/user/loginInfo.json", headers);

        System.out.println(responseString);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) jsonParser.parse(responseString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Response of Hanyang Api does not fit into JSON Format.");
        }
        jsonResponse = (JSONObject) jsonResponse.get("response");
        jsonResponse = (JSONObject) jsonResponse.get("item");

        studentId = (String) (jsonResponse.get("gaeinNo"));

        boolean isNew = false;
        UserDto newUser;
        UserDto savedUser;

        try {
            newUser = userService.getUserByStudentId(studentId);
        } catch (NotFoundOnDBException e) {
            newUser = new UserDto();
            newUser.setCreateTimeStampNow();
            isNew = true;
        } catch (FormatDoesNotMatchException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("서버 내부에 형식에 맞지 않는 레코드가 존재합니다.");
        }

        newUser.setStudentId((String) (jsonResponse.get("gaeinNo")));
        newUser.setName((String) (jsonResponse.get("userNm")));
        newUser.setNewToken();
        newUser.setApprovalTimeStampNow();

        ResponseEntity.BodyBuilder responseBodyBuilder = ResponseEntity.ok();

        URI location;
        try {
            location = new URI(Globals.serverUrl + "/login/" + studentId);
        } catch(URISyntaxException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Location of response does not meet URI syntax.");
        }

        if(isNew) {
            responseBodyBuilder = ResponseEntity.created(location);
            try {
                savedUser = userService.addUser(newUser);
            } catch (FormatDoesNotMatchException e) {
                e.printStackTrace();
                throw new InternalServerErrorException("서버 내부에 형식에 맞지 않는 레코드가 존재합니다.");
            }
        } else {
            try {
                savedUser = userService.updateUser(studentId, newUser);
            } catch (NotFoundOnDBException e) {
                e.printStackTrace();
                throw new InternalServerErrorException("Somethings wrong.");
            } catch (FormatDoesNotMatchException e) {
                e.printStackTrace();
                throw new InternalServerErrorException("서버 내부에 형식에 맞지 않는 레코드가 존재합니다.");
            }
        }

        String sosokId = (String) jsonResponse.get("sosokId");
        if(savedUser.getMaxPermission() == null) {
            if(CSE_SOSOK_ID.contains(sosokId)) {
                AuthorityDto newAuthority = AuthorityDto.builder()
                        .permission(AuthorityDto.Permission.USER)
                        .userDto(savedUser)
                        .build();

                try {
                    newAuthority = authorityService.addAuthority(newAuthority);
                } catch (FormatDoesNotMatchException e) {
                    e.printStackTrace();
                    throw new InternalServerErrorException("서버 내부에 형식에 맞지 않는 레코드가 존재합니다.");
                }
                savedUser.addAuthority(newAuthority);
            }
            else {
                throw new ForbiddenException("You're not CSE student.");
            }
        }

        return responseBodyBuilder.body(UserResponse.from(savedUser));

    }

    @GetMapping("auth/")
    public ResponseEntity<UserResponse> getUserUsingUserToken(@RequestHeader(value = "User-Token") String userToken) throws UnauthorizedException, InternalServerErrorException {
        UserDto user;
        try {
            user = userService.getUserByToken(userToken);
        } catch (FormatDoesNotMatchException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("서버 내부에 형식에 맞지 않는 레코드가 존재합니다.");
        } catch (NotFoundOnDBException e) {
            e.printStackTrace();
            throw new UnauthorizedException("There is no user in User-Token.");
        }
        return ResponseEntity.ok().body(UserResponse.from(user));
    }
}
