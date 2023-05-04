package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.error.exception.InvalidIndexException;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import com.example.beliemeserver.error.exception.NotFoundException;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService extends BaseService {
    public ItemService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<ItemDto> getListByStuff(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);

        return getItemListByStuffOrThrowInvalidIndexException(universityCode, departmentCode, stuffName);
    }

    public ItemDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);
        return itemDao.getByIndex(universityCode, departmentCode, stuffName, itemNum);
    }

    public ItemDto create(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String stuffName
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkStaffPermission(userToken, department);

        StuffDto stuff = getStuffOrThrowInvalidIndexException(universityCode, departmentCode, stuffName);
        ItemDto newItem = ItemDto.init(stuff, stuff.nextItemNum());

        if (newItem.num() > Constants.MAX_ITEM_NUM) {
            throw new ItemAmountLimitExceededException();
        }
        return itemDao.create(newItem);
    }

    protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(String universityCode, String departmentCode, String stuffName) {
        try {
            return itemDao.getListByStuff(universityCode, departmentCode, stuffName);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
