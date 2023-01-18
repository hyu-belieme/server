package com.example.beliemeserver.model.util.old;

import com.example.beliemeserver.model.dao.old.UserDao;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.exception.ForbiddenException;
import com.example.beliemeserver.exception.UnauthorizedException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.old.OldUserDto;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OldAuthCheck {
    UserDao userDao;

    public OldUserDto checkTokenAndGetUser(String token) throws UnauthorizedException, DataException {
        OldUserDto requester;
        try {
            requester = userDao.getUserByTokenData(token);
        } catch (NotFoundException e) {
            e.printStackTrace();
            throw new UnauthorizedException("token 인증에 실패하였습니다.");
        }
        return requester;
    }

    public void checkIfRequesterHasUserPermission(OldUserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasUserPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasStaffPermission(OldUserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasStaffPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasMasterPermission(OldUserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasMasterPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }

    public void checkIfRequesterHasDeveloperPermission(OldUserDto requester) throws ForbiddenException {
        if(!requester.getMaxPermission().hasDeveloperPermission()) {
            throw new ForbiddenException("권한이 부족합니다." + requester.getMaxPermission().toString());
        }
    }
}
