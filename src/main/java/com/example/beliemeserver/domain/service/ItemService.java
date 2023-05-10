package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialDataConfig;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ItemService extends BaseService {
    public ItemService(InitialDataConfig initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<ItemDto> getListByStuff(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        StuffDto targetStuff = getStuffOrThrowInvalidIndexException(stuffId);
        checkUserPermission(requester, targetStuff.department());
        return getItemListByStuffOrThrowInvalidIndexException(stuffId);
    }

    public ItemDto getById(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        ItemDto item = itemDao.getById(itemId);
        checkUserPermission(requester, item.stuff().department());
        return item;
    }

    public ItemDto create(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
        checkStaffPermission(requester, stuff.department());

        if (stuff.nextItemNum() > Constants.MAX_ITEM_NUM) {
            throw new ItemAmountLimitExceededException();
        }
        return itemDao.create(UUID.randomUUID(), stuffId, stuff.nextItemNum());
    }

    protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(UUID stuffId) {
        try {
            return itemDao.getListByStuff(stuffId);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
