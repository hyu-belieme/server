package com.example.beliemeserver.model.service;

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
        // TODO Need to implements.
        return null;
    }

    public UserDto updateUserFromHanyangUniversity(String apiToken) {
        // TODO Need to implements.
        return null;
    }
}
