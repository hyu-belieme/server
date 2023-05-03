package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.exception.IndexInvalidException;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewItemService extends NewBaseService {
    public NewItemService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<ItemDto> getListByStuff(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        StuffDto targetStuff = getStuffOrThrowInvalidIndexException(stuffId);
        checkUserPermission(userToken, targetStuff.department());

        return getItemListByStuffOrThrowInvalidIndexException(stuffId);
    }

    public ItemDto getById(
            @NonNull String userToken, @NonNull UUID itemId
    ) {
        ItemDto item = itemDao.getById(itemId);
        checkUserPermission(userToken, item.stuff().department());
        return item;
    }

    public ItemDto create(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        StuffDto stuff = getStuffOrThrowInvalidIndexException(stuffId);
        checkStaffPermission(userToken, stuff.department());

        ItemDto newItem = ItemDto.init(stuff, stuff.nextItemNum());
        if (newItem.num() > Constants.MAX_ITEM_NUM) {
            throw new ItemAmountLimitExceededException();
        }
        return itemDao.create(newItem);
    }

    protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(UUID stuffId) {
        try {
            return itemDao.getListByStuff(stuffId);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
