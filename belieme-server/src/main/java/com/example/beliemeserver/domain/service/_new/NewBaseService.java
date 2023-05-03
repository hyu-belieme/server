package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.config.initdata._new.InitialDataDtoAdapter;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.dto._new.UserDto;
import com.example.beliemeserver.domain.exception.IndexInvalidException;
import com.example.beliemeserver.domain.exception.PermissionDeniedException;
import com.example.beliemeserver.domain.exception.TokenExpiredException;
import com.example.beliemeserver.error.exception.NotFoundException;
import com.example.beliemeserver.error.exception.UnauthorizedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public abstract class NewBaseService {
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

    public NewBaseService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
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

    protected DepartmentDto getDepartmentOrThrowInvalidIndexException(UUID departmentId) {
        try {
            return departmentDao.getById(departmentId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    protected StuffDto getStuffOrThrowInvalidIndexException(UUID stuffId) {
        try {
            return stuffDao.getById(stuffId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }

    protected ItemDto getItemOrThrowInvalidIndexException(UUID itemId) {
        try {
            return itemDao.getById(itemId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
