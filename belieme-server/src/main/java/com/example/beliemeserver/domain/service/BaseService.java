package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.config.initdata.InitialDataDtoAdapter;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.domain.exception.IndexInvalidException;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.exception.TokenExpiredException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseService {
    protected final InitialDataDtoAdapter initialData;
    protected final UniversityDao universityDao;
    protected final DepartmentDao departmentDao;
    protected final UserDao userDao;
    protected final MajorDao majorDao;
    protected final AuthorityDao authorityDao;
    protected final StuffDao stuffDao;
    protected final ItemDao itemDao;
    protected final HistoryDao historyDao;

    public static final long TOKEN_EXPIRED_TIME = 3L * 30 * 24 * 60 * 60;

    public BaseService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        this.initialData = new InitialDataDtoAdapter(initialData);
        this.universityDao = universityDao;
        this.departmentDao = departmentDao;
        this.userDao = userDao;
        this.majorDao = majorDao;
        this.authorityDao = authorityDao;
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.historyDao = historyDao;
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
        long currentTime = (System.currentTimeMillis() / 1000);
        long expiredAt = user.approvedAt() + TOKEN_EXPIRED_TIME;
        if (currentTime > expiredAt) {
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