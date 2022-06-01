package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.*;
import com.example.beliemeserver.model.exception.*;

import com.example.beliemeserver.model.util.AuthCheck;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final StuffDao stuffDao;

    private final ItemDao itemDao;

    private final AuthCheck authCheck;

    public ItemService(StuffDao stuffDao, ItemDao itemDao, UserDao userDao) {
        this.stuffDao = stuffDao;
        this.itemDao = itemDao;
        this.authCheck = new AuthCheck(userDao);
    }

    public StuffDto postItem(String userToken, String stuffName, Integer amount) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, ConflictException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
//        authCheck.checkIfRequesterHasStaffPermission(requester);
        authCheck.checkIfRequesterHasUserPermission(requester);

        StuffDto targetStuff = stuffDao.getStuffByNameData(stuffName);

        int realAmount = 1;
        if(amount != null) {
            realAmount = amount;
        }

        for (int i = 0; i < realAmount; i++) {
            itemDao.addItemData(
                    ItemDto.builder()
                            .stuff(targetStuff)
                            .lastHistory(null)
                            .build()
            );
        }

        return stuffDao.getStuffByNameData(stuffName);
    }
}
