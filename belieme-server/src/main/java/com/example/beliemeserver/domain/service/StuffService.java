package com.example.beliemeserver.domain.service;

import com.example.beliemeserver.config.initdata.InitialData;
import com.example.beliemeserver.domain.dao.*;
import com.example.beliemeserver.domain.dto.DepartmentDto;
import com.example.beliemeserver.domain.dto.ItemDto;
import com.example.beliemeserver.domain.dto.StuffDto;
import com.example.beliemeserver.domain.exception.ItemAmountLimitExceededException;
import com.example.beliemeserver.domain.util.Constants;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuffService extends BaseService {
    public StuffService(InitialData initialData, UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(initialData, universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<StuffDto> getListByDepartment(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);

        return stuffDao.getListByDepartment(universityCode, departmentCode);
    }

    public StuffDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String name
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkUserPermission(userToken, department);

        return stuffDao.getByIndex(universityCode, departmentCode, name);
    }

    public StuffDto create(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String name, @NonNull String thumbnail, Integer amount
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
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
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String name,
            String newName, String newThumbnail
    ) {
        DepartmentDto department = getDepartmentOrThrowInvalidIndexException(universityCode, departmentCode);
        checkStaffPermission(userToken, department);

        StuffDto oldStuff = stuffDao.getByIndex(universityCode, departmentCode, name);

        if (newName == null && newThumbnail == null) return oldStuff;
        if (newName == null) newName = oldStuff.name();
        if (newThumbnail == null) newThumbnail = oldStuff.thumbnail();

        StuffDto newStuff = StuffDto.init(department, newName, newThumbnail);
        return stuffDao.update(universityCode, departmentCode, name, newStuff);
    }
}
