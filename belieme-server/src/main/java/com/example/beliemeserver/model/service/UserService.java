package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.AuthorityDto;
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

    public List<UserDto> getAllList(@NonNull String userToken) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public List<UserDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        // TODO Need to implements.
        return new ArrayList<>();
    }

    public UserDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String studentId
    ) {
        // TODO Need to implements.
        return null;
    }

    public UserDto getByToken(
            @NonNull String userToken
    ) {
        // TODO Need to implements.
        return null;
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
