package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface ItemDao {
    public List<ItemDto> getItemsByStuffNameData(String stuffName) throws DataException;
    ItemDto getItemByStuffNameAndItemNumData(String stuffName, int itemNum) throws NotFoundException, DataException;
    public ItemDto addItemData(ItemDto newItem) throws ConflictException, NotFoundException, DataException;
    public ItemDto updateItemData(String stuffName, int itemNum, ItemDto itemDto) throws NotFoundException, DataException;
}
