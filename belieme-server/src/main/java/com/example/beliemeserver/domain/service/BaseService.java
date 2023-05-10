package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialDataConfig;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.exception.TokenExpiredException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public abstract class BaseService {
    protected final InitialDataConfig initialData;
    protected final UniversityDao universityDao;
    protected final DepartmentDao departmentDao;
    protected final UserDao userDao;
    protected final MajorDao majorDao;
    protected final AuthorityDao authorityDao;
    protected final StuffDao stuffDao;
    protected final ItemDao itemDao;
    protected final HistoryDao historyDao;

    public static final long TOKEN_EXPIRED_TIME = 3L * 30 * 24 * 60 * 60;

    public BaseService(InitialDataConfig initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        this.initialData = initialData;
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

    protected void checkUserPermission(UserDto requester, DepartmentDto department) {
        if (!requester.getMaxPermission(department).hasUserPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkStaffPermission(UserDto requester, DepartmentDto department) {
        if (!requester.getMaxPermission(department).hasStaffPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkMasterPermission(UserDto requester, DepartmentDto department) {
        if (!requester.getMaxPermission(department).hasMasterPermission()) {
            throw new PermissionDeniedException();
        }
    }

    protected void checkDeveloperPermission(UserDto requester) {
        if (!requester.isDeveloper()) {
            throw new PermissionDeniedException();
        }
    }

    protected DepartmentDto getDepartmentOrThrowInvalidIndexException(UUID departmentId) {
        try {
            return departmentDao.getById(departmentId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected StuffDto getStuffOrThrowInvalidIndexException(UUID stuffId) {
        try {
            return stuffDao.getById(stuffId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected ItemDto getItemOrThrowInvalidIndexException(UUID itemId) {
        try {
            return itemDao.getById(itemId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }

    protected long currentTime() {
        return System.currentTimeMillis() / 1000;
    }
}
