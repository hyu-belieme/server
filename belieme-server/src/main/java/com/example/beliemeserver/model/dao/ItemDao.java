package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList() throws DataException;

    @Transactional
    List<ItemDto> getListByStuff(
            String universityCode, String departmentCode, String stuffName)
            throws DataException, NotFoundException;

    @Transactional
    ItemDto getByIndex(
            String universityCode, String departmentCode,
            String stuffName, int itemNum)
            throws NotFoundException, DataException;

    @Transactional
    ItemDto create(ItemDto newItem)
            throws ConflictException, NotFoundException, DataException;

    @Transactional
    ItemDto update(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, ItemDto newItem)
            throws ConflictException, NotFoundException, DataException;
}
