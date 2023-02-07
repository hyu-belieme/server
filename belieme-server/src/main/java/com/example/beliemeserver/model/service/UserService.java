package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.MethodNotAllowedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public UserDto updateUserFromHanyangUniversity(String apiToken) {
        // TODO Need to implements.
        return null;
    }
}
