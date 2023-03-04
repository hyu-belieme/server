package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.ItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList();

    @Transactional
    List<ItemDto> getListByStuff(String universityCode,
                                 String departmentCode, String stuffName);

    @Transactional
    ItemDto getByIndex(String universityCode, String departmentCode,
                       String stuffName, int itemNum);

    @Transactional
    ItemDto create(ItemDto newItem);

    @Transactional
    ItemDto update(String universityCode, String departmentCode,
                   String stuffName, int itemNum, ItemDto newItem);
}
