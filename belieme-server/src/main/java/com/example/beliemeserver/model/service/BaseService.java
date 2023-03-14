package com.example.beliemeserver.model.service;

import com.example.beliemeserver.common.InitialInfos;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.TokenExpiredException;
import com.example.beliemeserver.model.exception.IndexInvalidException;
import com.example.beliemeserver.model.exception.PermissionDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public abstract class BaseService {
    private final InitialInfos initialInfos;

    protected final UniversityDao universityDao;
    protected final DepartmentDao departmentDao;
    protected final UserDao userDao;
    protected final MajorDao majorDao;
    protected final AuthorityDao authorityDao;
    protected final StuffDao stuffDao;
    protected final ItemDao itemDao;
    protected final HistoryDao historyDao;

    public static final long TOKEN_EXPIRED_TIME = 3L * 30 * 24 * 60 * 60;

    public BaseService(InitialInfos initialInfos, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        this.initialInfos = initialInfos;
        this.universityDao = universityDao;
        this.departmentDao = departmentDao;
        this.userDao = userDao;
        this.majorDao = majorDao;
        this.authorityDao = authorityDao;
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.historyDao = historyDao;
    }

    public List<UniversityDto> getInitialUniversities() {
        Map<String, UniversityDto> dtoMap = initialInfos.universities();
        return new ArrayList<>(dtoMap.values());
    }

    public List<DepartmentDto> getInitialDepartments() {
        Map<String, DepartmentDto> output = initialInfos.departments();
        return new ArrayList<>(output.values());
    }

    public List<InitialInfos.UserInfo> getInitialUserInfos() {
        return initialInfos.userInfos();
    }

    public InitialInfos.UniversityInfo getUniversityInfoByKey(String key) {
        return initialInfos.universityInfos().get(key);
    }

    protected UserDto validateTokenAndGetUser(String token) {
        UserDto user = checkTokenAndGetUser(token);
        validateUser(user);
        return user;
    }

    private UserDto checkTokenAndGetUser(String token) {
        try {
            return userDao.getByToken(token);
        } catch (NotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    private void validateUser(UserDto user) {
        long currentTimestamp = (System.currentTimeMillis() / 1000);
        long expiredTimestamp = user.approvalTimeStamp() + TOKEN_EXPIRED_TIME;
        if (currentTimestamp > expiredTimestamp) {
            throw new TokenExpiredException();
        }
    }

    protected void checkUserPermission(String token, DepartmentDto department) {
        UserDto requester = validateTokenAndGetUser(token);
        if (!requester.getMaxPermission(department).hasUserPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkStaffPermission(String token, DepartmentDto department) {
        UserDto requester = validateTokenAndGetUser(token);
        if (!requester.getMaxPermission(department).hasStaffPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkMasterPermission(String token, DepartmentDto department) {
        UserDto requester = validateTokenAndGetUser(token);
        if (!requester.getMaxPermission(department).hasMasterPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkDeveloperPermission(String token) {
        UserDto requester = validateTokenAndGetUser(token);
        if (!requester.isDeveloper()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkUserPermission(DepartmentDto department, UserDto requester) {
        if (!requester.getMaxPermission(department).hasUserPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkStaffPermission(DepartmentDto department, UserDto requester) {
        if (!requester.getMaxPermission(department).hasStaffPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkMasterPermission(DepartmentDto department, UserDto requester) {
        if (!requester.getMaxPermission(department).hasMasterPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected DepartmentDto getDepartmentOrThrowInvalidIndexException(String universityCode, String departmentCode) {
        try {
            return departmentDao.getByIndex(universityCode, departmentCode);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    protected StuffDto getStuffOrThrowInvalidIndexException(String universityCode, String departmentCode, String stuffName) {
        try {
            return stuffDao.getByIndex(universityCode, departmentCode, stuffName);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    protected ItemDto getItemOrThrowInvalidIndexException(String universityCode, String departmentCode, String stuffName, int itemNum) {
        try {
            return itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
