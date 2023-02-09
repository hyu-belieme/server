package com.example.beliemeserver.model.service;

import com.example.beliemeserver.common.Globals;
import com.example.beliemeserver.exception.BadGateWayException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.MethodNotAllowedException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.util.HttpRequest;
import lombok.NonNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService extends BaseService {
    public UserService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<UserDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkMasterPermission(userToken, department);

        List<UserDto> output = new ArrayList<>();
        for(UserDto user : userDao.getAllList()) {
            if(user.getMaxPermission(department).hasMorePermission(AuthorityDto.Permission.USER)) {
                output.add(user);
            }
        }

        return output;
    }

    public UserDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String studentId
    ) {
        checkDeveloperPermission(userToken);
        return userDao.getByIndex(universityCode, studentId);
    }

    public UserDto getByToken(
            @NonNull String userToken
    ) {
        return checkTokenAndGetUser(userToken);
    }

    public UserDto updateAuthority(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String studentId,
            @NonNull String authorityUniversityCode,
            @NonNull String authorityDepartmentCode,
            AuthorityDto.Permission newPermission
    ) {
        UserDto requester = checkTokenAndGetUser(userToken);

        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(authorityUniversityCode, authorityDepartmentCode);
        checkMasterPermission(department, requester);

        UserDto targetUser = userDao.getByIndex(universityCode, studentId);
        if(targetUser.isDeveloper()) {
            throw new MethodNotAllowedException();
        }
        if(newPermission != null && newPermission.hasDeveloperPermission()) {
            throw new MethodNotAllowedException();
        }

        if(!requester.isDeveloper()) {
            if(targetUser.getMaxPermission(department).hasMasterPermission()) {
                throw new ForbiddenException();
            }
            if(newPermission != null && newPermission.hasMasterPermission()) {
                throw new ForbiddenException();
            }
        }

        UserDto newUser = targetUser.withAuthorityUpdate(department, newPermission);
        return userDao.update(universityCode, studentId, newUser);
    }

    public UserDto updateUserFromHanyangUniversity(@NonNull String apiToken) {
        JSONObject jsonResponse = HttpRequest.getUserInfoFromHanyangApi(apiToken);
        String studentId = (String) (jsonResponse.get("gaeinNo"));
        String name = (String) (jsonResponse.get("userNm"));
        String sosokId = (String) jsonResponse.get("sosokId");
        List<String> majorCodes = List.of(sosokId);

        return updateOrInitAndSave(Globals.HANYANG_UNIVERSITY_CODE, studentId, name, majorCodes);
    }

    private UserDto updateOrInitAndSave(String universityCode, String studentId, String name, List<String> majorCodes) {
        boolean isNew = false;

        UserDto newUser;
        UniversityDto university = universityDao.getByIndex(universityCode);
        try {
            newUser = userDao.getByIndex(universityCode, studentId);
        } catch (NotFoundException e) {
            isNew = true;
            newUser = UserDto.init(university, studentId, name);
        }
        List<MajorDto> newBaseMajors = newMajors(majorCodes, university, newUser);
        newUser = newUser.withName(name)
                .withApprovalTimeStamp(currentTimestamp())
                .withMajors(newBaseMajors)
                .withToken(UUID.randomUUID().toString());

        if(isNew) return userDao.create(newUser);
        return userDao.update(universityCode, studentId, newUser);
    }

    private List<MajorDto> newMajors(List<String> majorCodes, UniversityDto university, UserDto newUser) {
        List<MajorDto> newBaseMajors = newUser.majors();
        if(majorCodes != null) {
            newBaseMajors = new ArrayList<>();
            for(String majorCode : majorCodes) {
                newBaseMajors.add(getMajorOrCreate(university, majorCode));
            }
        }
        return newBaseMajors;
    }

    private MajorDto getMajorOrCreate(UniversityDto university, String majorCode) {
        try {
            return majorDao.getByIndex(university.code(), majorCode);
        } catch (NotFoundException e) {
            return majorDao.create(new MajorDto(university, majorCode));
        }
    }

    private long currentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }
}
