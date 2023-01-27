package com.example.beliemeserver.model.service;

import com.example.beliemeserver.exception.InvalidIndexException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.DepartmentDto;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.dto.StuffDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService extends BaseService {
    public ItemService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
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
        // TODO Need to implements.
        return null;
    }

    protected List<ItemDto> getItemListByStuffOrThrowInvalidIndexException(String universityCode, String departmentCode, String stuffName) {
        try {
            return itemDao.getListByStuff(universityCode, departmentCode, stuffName);
        } catch (NotFoundException e) {
            throw new InvalidIndexException();
        }
    }
}
