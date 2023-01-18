package com.example.beliemeserver.model.service.old;

import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.exception.*;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.model.util.old.OldAuthCheck;
import com.example.beliemeserver.model.util.old.OldHttpRequest;

import com.example.beliemeserver.model.dao.old.AuthorityDao;
import com.example.beliemeserver.model.dao.old.UserDao;

import com.example.beliemeserver.model.dto.old.OldAuthorityDto;
import com.example.beliemeserver.model.dto.old.OldUserDto;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OldUserService {
    private static final String client_id = "a4b1abe746f384c3d43fa82a17f222";
    private static final Set<String> CSE_SOSOK_ID = new HashSet<>(Arrays.asList("FH04067"));

    private final UserDao userDao;
    private final AuthorityDao authorityDao;
    private final OldAuthCheck authCheck;

    public OldUserService(UserDao userDao, AuthorityDao authorityDao) {
        this.userDao = userDao;
        this.authorityDao = authorityDao;
        this.authCheck = new OldAuthCheck(userDao);
    }

    public OldUserDto getUserInfoFromUnivApiByApiToken(String apiToken) throws DataException, ConflictException, NotFoundException, ForbiddenException, BadGateWayException {
        String studentId, name, sosokId;
        if(apiToken.equals(Globals.developerApiToken)) {
            studentId = "dev";
            name = "dev";
            sosokId = "dev";
        } else {
            JSONObject jsonResponse = getStudentInfoFromHanyangApi(apiToken);
            studentId = (String) (jsonResponse.get("gaeinNo"));
            name = (String) (jsonResponse.get("userNm"));
            sosokId = (String) jsonResponse.get("sosokId");
        }

        OldUserDto savedUser = setUserAndUpdateDB(studentId, name);

        if(savedUser.getMaxPermission() == null) {
            OldAuthorityDto newAuthority;
            if(apiToken.equals(Globals.developerApiToken)) {
                newAuthority = addAuthority(savedUser, OldAuthorityDto.Permission.DEVELOPER);
            } else {
                newAuthority = checkAndAddUserAuthority(savedUser, sosokId);
            }
            savedUser.addAuthority(newAuthority);
        }

        return savedUser;
    }

    public OldUserDto getUserByUserToken(String userToken) throws DataException, UnauthorizedException {
        return authCheck.checkTokenAndGetUser(userToken);
    }

    public OldUserDto makeUserHavePermission(String userToken, String studentId, OldAuthorityDto.Permission permission) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, ConflictException, MethodNotAllowedException {
        OldUserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasDeveloperPermission(requester);

        OldUserDto target = userDao.getUserByStudentIdData(studentId);

        if(target.getMaxPermission() == OldAuthorityDto.Permission.DEVELOPER) {
            throw new MethodNotAllowedException("DEVELOPER의 권한을 변경할 수 없습니다.");
        } else if(target.getMaxPermission() == null) {
            addAuthority(target, permission);
        } else {
            updateAuthority(target, permission);
        }
        return userDao.getUserByStudentIdData(studentId);
    }

    private JSONObject getStudentInfoFromHanyangApi(String apiToken) throws BadGateWayException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "https://api.hanyang.ac.kr/");
        headers.put("client_id", client_id);
        headers.put("swap_key", Long.toString(System.currentTimeMillis()/1000));
        headers.put("access_token", apiToken);
        String responseString = null;
        responseString = OldHttpRequest.sendGetRequest("https://api.hanyang.ac.kr/rs/user/loginInfo.json", headers);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonResponse;
        try {
            jsonResponse = (JSONObject) jsonParser.parse(responseString);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BadGateWayException("Response of Hanyang Api does not fit into JSON Format.");
        }
        jsonResponse = (JSONObject) jsonResponse.get("response");
        jsonResponse = (JSONObject) jsonResponse.get("item");
        return jsonResponse;
    }

    private OldUserDto setUserAndUpdateDB(String studentId, String name) throws DataException, ConflictException, NotFoundException {
        boolean isNew = false;

        OldUserDto newUser;
        try {
            newUser = userDao.getUserByStudentIdData(studentId);
        } catch (NotFoundException e) {
            isNew = true;
            newUser = new OldUserDto();
            newUser.setCreateTimeStampNow();
        }

        newUser.setStudentId(studentId);
        newUser.setName(name);
        newUser.setNewToken();
        newUser.setApprovalTimeStampNow();

        if(isNew) {
            return userDao.addUserData(newUser);
        } else {
            return userDao.updateUserData(studentId, newUser);
        }
    }

    private OldAuthorityDto checkAndAddUserAuthority(OldUserDto savedUser, String sosokId) throws ConflictException, DataException, ForbiddenException {
        if(CSE_SOSOK_ID.contains(sosokId)) {
            return addAuthority(savedUser, OldAuthorityDto.Permission.USER);
        } else {
            throw new ForbiddenException("You're not CSE student.");
        }
    }

    private OldAuthorityDto addAuthority(OldUserDto savedUser, OldAuthorityDto.Permission permission) throws ConflictException, DataException {
        OldAuthorityDto newAuthority = OldAuthorityDto.builder()
                .permission(permission)
                .userDto(savedUser)
                .build();

        return authorityDao.addAuthorityData(newAuthority);
    }

    private OldAuthorityDto updateAuthority(OldUserDto savedUser, OldAuthorityDto.Permission permission) throws DataException {
        OldAuthorityDto newAuthority = OldAuthorityDto.builder()
                .permission(permission)
                .userDto(savedUser)
                .build();

        return authorityDao.updateAuthorityData(savedUser.getStudentId(), newAuthority);
    }
}
