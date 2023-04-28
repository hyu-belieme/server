package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.ItemDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemDao {
    @Transactional
    List<ItemDto> getAllList();

    @Transactional
    List<ItemDto> getListByStuff(String universityName,
                                 String departmentName, String stuffName);

    @Transactional
    ItemDto getByIndex(String universityName, String departmentName,
                       String stuffName, int itemNum);

    @Transactional
    ItemDto create(ItemDto newItem);

    @Transactional
    ItemDto update(String universityName, String departmentName,
                   String stuffName, int itemNum, ItemDto newItem);
}
