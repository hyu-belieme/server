package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.AuthorityDto;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public abstract class BaseService {
    protected final UniversityDao universityDao;
    protected final DepartmentDao departmentDao;
    protected final UserDao userDao;
    protected final MajorDao majorDao;
    protected final AuthorityDao authorityDao;
    protected final StuffDao stuffDao;
    protected final ItemDao itemDao;
    protected final HistoryDao historyDao;

    public BaseService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        this.universityDao = universityDao;
        this.departmentDao = departmentDao;
        this.userDao = userDao;
        this.majorDao = majorDao;
        this.authorityDao = authorityDao;
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.historyDao = historyDao;
    }

    protected UserDto checkTokenAndGetUser(String token) {
        try {
            return userDao.getByToken(token);
        } catch (NotFoundException e) {
            throw new UnauthorizedException();
        }
    }

    protected void checkUserPermission(String token, DepartmentDto department) {
        UserDto requester = checkTokenAndGetUser(token);
        if(!requester.getMaxPermission(department).hasUserPermission()) {
            throw new ForbiddenException();
        }
    }

    protected void checkStaffPermission(String token, DepartmentDto department) {
        UserDto requester = checkTokenAndGetUser(token);
        if(!requester.getMaxPermission(department).hasStaffPermission()) {
            throw new ForbiddenException();
        }
    }

    protected void checkMasterPermission(String token, DepartmentDto department) {
        UserDto requester = checkTokenAndGetUser(token);
        if(!requester.getMaxPermission(department).hasMasterPermission()) {
            throw new ForbiddenException();
        }
    }

    protected void checkDeveloperPermission(String token) {
        UserDto requester = checkTokenAndGetUser(token);
        if(!requester.isDeveloper()) {
            throw new ForbiddenException();
        }
    }
}
