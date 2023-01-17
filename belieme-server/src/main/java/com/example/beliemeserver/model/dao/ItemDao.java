package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.FormatDoesNotMatchException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList() throws FormatDoesNotMatchException;

    @Transactional
    List<ItemDto> getListByStuff(
            String universityCode, String departmentCode, String stuffName)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    ItemDto getByIndex(
            String universityCode, String departmentCode,
            String stuffName, int itemNum)
            throws NotFoundException, FormatDoesNotMatchException;

    @Transactional
    ItemDto create(ItemDto newItem)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;

    @Transactional
    ItemDto update(
            String universityCode, String departmentCode,
            String stuffName, int itemNum, ItemDto newItem)
            throws ConflictException, NotFoundException, FormatDoesNotMatchException;
}
