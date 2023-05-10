package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialDataConfig;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.dto.UserDto;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StuffService extends BaseService {
    public StuffService(InitialDataConfig initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<StuffDto> getListByDepartment(
            @NonNull String userToken, @NonNull UUID departmentId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        checkUserPermission(requester, department);

        return stuffDao.getListByDepartment(departmentId);
    }

    public StuffDto getById(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        StuffDto stuff = stuffDao.getById(stuffId);
        checkUserPermission(requester, stuff.department());

        return stuff;
    }

    public StuffDto create(
            @NonNull String userToken, @NonNull UUID departmentId,
            @NonNull String name, @NonNull String thumbnail, Integer amount
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        System.out.println(departmentId);
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        checkStaffPermission(requester, department);

        StuffDto newStuff = stuffDao.create(UUID.randomUUID(), departmentId, name, thumbnail);

        if (amount == null) return newStuff;
        if (amount > Constants.MAX_ITEM_NUM) throw new ItemAmountLimitExceededException();

        for (int i = 0; i < amount; i++) {
            ItemDto newItem = itemDao.create(UUID.randomUUID(), newStuff.id(), i + 1);
            newStuff = newStuff.withItemAdd(newItem);
        }
        return newStuff;
    }

    public StuffDto update(
            @NonNull String userToken, @NonNull UUID stuffId,
            String newName, String newThumbnail
    ) {
        UserDto requester = validateTokenAndGetUser(userToken);
        StuffDto oldStuff = stuffDao.getById(stuffId);
        checkStaffPermission(requester, oldStuff.department());

        if (newName == null && newThumbnail == null) return oldStuff;
        if (newName == null) newName = oldStuff.name();
        if (newThumbnail == null) newThumbnail = oldStuff.thumbnail();

        return stuffDao.update(stuffId, oldStuff.department().id(), newName, newThumbnail);
    }
}
