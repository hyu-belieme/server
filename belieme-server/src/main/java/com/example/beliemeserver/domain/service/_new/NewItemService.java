package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.exception.IndexInvalidException;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewItemService extends NewBaseService {
    public NewItemService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<ItemDto> getListByStuff(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName, @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkUserPermission(userToken, department);

        return getItemListByStuffOrThrowInvalidIndexException(universityName, departmentName, stuffName);
    }

    public ItemDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkUserPermission(userToken, department);
        return itemDao.getByIndex(universityName, departmentName, stuffName, itemNum);
    }

    public ItemDto create(
            @NonNull String userToken,
            @NonNull String universityName, @NonNull String departmentName, @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityName, departmentName);
        checkStaffPermission(userToken, department);

        StuffDto stuff = getStuffOrThrowInvalidIndexException(universityName, departmentName, stuffName);
        ItemDto newItem = ItemDto.init(stuff, stuff.nextItemNum());

        if (newItem.num() > Constants.MAX_ITEM_NUM) {
            throw new ItemAmountLimitExceededException();
        }
        return itemDao.create(newItem);
    }

    protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(String universityName, String departmentName, String stuffName) {
        try {
            return itemDao.getListByStuff(universityName, departmentName, stuffName);
        } catch (NotFoundException e) {
            throw new IndexInvalidException();
        }
    }
}
