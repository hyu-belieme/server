package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;

import java.util.List;

public interface ItemDao {
    public List<OldItemDto> getItemsByStuffNameData(String stuffName) throws DataException;
    OldItemDto getItemByStuffNameAndItemNumData(String stuffName, int itemNum) throws NotFoundException, DataException;
    public OldItemDto addItemData(OldItemDto newItem) throws ConflictException, NotFoundException, DataException;
    public OldItemDto updateItemData(String stuffName, int itemNum, OldItemDto itemDto) throws NotFoundException, DataException;
}
