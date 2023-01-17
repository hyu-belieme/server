package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface ItemDao {
    List<ItemDto> getAllList() throws DataException;

    List<ItemDto> getListByStuff(
            String universityCode, String departmentCode, String stuffName)
            throws DataException, NotFoundException;

    ItemDto getByIndex(
            String universityCode, String departmentCode,
            String stuffName, int itemNum)
            throws NotFoundException, DataException;

    ItemDto create(ItemDto newItem)
            throws ConflictException, NotFoundException, DataException;

    ItemDto update(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, ItemDto newItem)
            throws ConflictException, NotFoundException, DataException;
}
