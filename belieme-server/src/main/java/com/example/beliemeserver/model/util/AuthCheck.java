package com.example.beliemeserver.model.util;

import com.example.beliemeserver.model.dao.UserDao;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ForbiddenException;
import com.example.beliemeserver.model.exception.UnauthorizedException;
import com.example.beliemeserver.model.exception.NotFoundException;
import com.example.beliemeserver.model.dto.UserDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthCheck {
    UserDao userDao;

    public UserDto checkTokenAndGetUser(String token) throws UnauthorizedException, DataException {
        UserDto requester;
        try {
            requester = userDao.getUserByTokenData(token);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new UnauthorizedException("token 인증에 실패하였습니다.");
        }
        return requester;
    }

    public void checkIfRequesterHasUserPermission(UserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasUserPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasStaffPermission(UserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasStaffPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasMasterPermission(UserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasMasterPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasDeveloperPermission(UserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasDeveloperPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }
}
