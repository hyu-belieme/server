package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemDaoImpl implements ItemDao {
    @Override
    public List<ItemDto> getAllList() throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public List<ItemDto> getListByStuff(String universityCode, String departmentCode, String stuffName) throws DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public ItemDto getByIndex(String universityCode, String departmentCode, String stuffName, int itemNum) throws NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public ItemDto create(ItemDto newItem) throws ConflictException, NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }

    @Override
    public ItemDto update(String universityCode, String departmentCode, String stuffName, int itemNum, ItemDto newItem) throws ConflictException, NotFoundException, DataException {
        // TODO Need Implement
        return null;
    }
}
