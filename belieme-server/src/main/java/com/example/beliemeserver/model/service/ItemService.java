package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.old.ItemDao;
import com.example.beliemeserver.model.dao.old.StuffDao;
import com.example.beliemeserver.model.dao.old.UserDao;
import com.example.beliemeserver.model.dto.old.ItemDto;
import com.example.beliemeserver.model.dto.old.StuffDto;
import com.example.beliemeserver.model.dto.old.UserDto;
import com.example.beliemeserver.model.exception.*;

import com.example.beliemeserver.model.util.AuthCheck;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<ItemDto> getItems(String userToken, String stuffName) throws DataException, UnauthorizedException, ForbiddenException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

        return itemDao.getItemsByStuffNameData(stuffName);
    }

    public StuffDto postItem(String userToken, String stuffName, Integer amount) throws DataException, UnauthorizedException, ForbiddenException, NotFoundException, ConflictException {
        UserDto requester = authCheck.checkTokenAndGetUser(userToken);
        authCheck.checkIfRequesterHasStaffPermission(requester);

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
