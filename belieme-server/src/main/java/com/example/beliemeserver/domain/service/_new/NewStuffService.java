package com.example.beliemeserver.domain.service._new;

import com.example.beliemeserver.config.initdata._new.InitialData;
import com.example.beliemeserver.domain.dao._new.*;
import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.ItemDto;
import com.example.beliemeserver.domain.dto._new.StuffDto;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class NewStuffService extends NewBaseService {
    public NewStuffService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<StuffDto> getListByDepartment(
            @NonNull String userToken, @NonNull UUID departmentId
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        checkUserPermission(userToken, department);

        return stuffDao.getListByDepartment(departmentId);
    }

    public StuffDto getById(
            @NonNull String userToken, @NonNull UUID stuffId
    ) {
        StuffDto stuff = stuffDao.getById(stuffId);
        checkUserPermission(userToken, stuff.department());

        return stuff;
    }

    public StuffDto create(
            @NonNull String userToken, @NonNull UUID departmentId,
            @NonNull String name, @NonNull String thumbnail, Integer amount
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(departmentId);
        checkStaffPermission(userToken, department);

        StuffDto newStuff = StuffDto.init(department, name, thumbnail);
        StuffDto output = newStuff;
        newStuff = stuffDao.create(newStuff);

        if (amount == null) return output;
        if (amount > Constants.MAX_ITEM_NUM) throw new ItemAmountLimitExceededException();

        for (int i = 0; i < amount; i++) {
            ItemDto newItem = itemDao.create(ItemDto.init(newStuff, i + 1));
            output = output.withItemAdd(newItem);
        }
        return output;
    }

    public StuffDto update(
            @NonNull String userToken, @NonNull UUID stuffId,
            String newName, String newThumbnail
    ) {
        StuffDto oldStuff = stuffDao.getById(stuffId);
        checkStaffPermission(userToken, oldStuff.department());

        if (newName == null && newThumbnail == null) return oldStuff;
        if (newName == null) newName = oldStuff.name();
        if (newThumbnail == null) newThumbnail = oldStuff.thumbnail();

        StuffDto newStuff = StuffDto.init(oldStuff.department(), newName, newThumbnail);
        return stuffDao.update(stuffId, newStuff);
    }
}
