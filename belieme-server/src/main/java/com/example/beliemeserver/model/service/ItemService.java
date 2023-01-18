package com.example.beliemeserver.model.service;

import com.example.beliemeserver.model.dao.*;
import com.example.beliemeserver.model.dto.ItemDto;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService extends BaseService {
    public ItemService(UniversityDao universityDao, DepartmentDao departmentDao, UserDao userDao, MajorDao majorDao, AuthorityDao authorityDao, StuffDao stuffDao, ItemDao itemDao, HistoryDao historyDao) {
        super(universityDao, departmentDao, userDao, majorDao, authorityDao, stuffDao, itemDao, historyDao);
    }

    public List<ItemDto> getListByStuffs(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String stuffName
    ) {
       // TODO Need to implements.
       return new ArrayList<>();
    }

    public ItemDto getByIndex(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode,
            @NonNull String stuffName, int itemNum
    ) {
        // TODO Need to implements.
        return null;
    }

    public ItemDto create(
            @NonNull String userToken,
            @NonNull String universityCode, @NonNull String departmentCode, @NonNull String stuffName
    ) {
        // TODO Need to implements.
        return null;
    }
}
