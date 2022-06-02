package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.ItemEntity;
import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.entity.id.ItemId;
import com.example.beliemeserver.data.repository.ItemRepository;
import com.example.beliemeserver.data.repository.StuffRepository;
import com.example.beliemeserver.model.dao.ItemDao;
import com.example.beliemeserver.model.dto.ItemDto;
import com.example.beliemeserver.model.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ItemDaoImpl implements ItemDao {
    @Autowired
    StuffRepository stuffRepository;

    @Autowired
    ItemRepository itemRepository;

    @Override
    public List<ItemDto> getItemsByStuffNameData(String stuffName) throws DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            return new ArrayList<>();
        }

        List<ItemDto> itemDtoList = new ArrayList<>();
        Iterator<ItemEntity> iterator = itemRepository.findByStuffId(stuffEntity.getId()).iterator();
        while(iterator.hasNext()) {
            itemDtoList.add(iterator.next().toItemDto());
        }
        return itemDtoList;
    }

    @Override
    public ItemDto getItemByStuffNameAndItemNumData(String stuffName, int itemNum) throws NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        ItemId itemId = new ItemId(stuffEntity.getId(), itemNum);

        ItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
        if(itemEntity == null) {
            throw new NotFoundException();
        }
        return itemEntity.toItemDto();
    }

    @Override
    public ItemDto addItemData(ItemDto newItem) throws ConflictException, NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(newItem.getStuff().getName()).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        int newItemNum = stuffEntity.getAndIncrementNextItemNum();
        ItemId itemId = new ItemId(stuffEntity.getId(), newItemNum);
        if(itemRepository.existsById(itemId)) {
            throw new ConflictException();
        }

        ItemEntity newItemEntity = ItemEntity.builder()
                .stuffId(stuffEntity.getId())
                .num(newItemNum)
                .lastHistoryNum(null)
                .nextHistoryNum(1)
                .build();

        ItemEntity savedItemEntity = itemRepository.save(newItemEntity);
        itemRepository.refresh(savedItemEntity);
        stuffRepository.refresh(stuffEntity);
        return savedItemEntity.toItemDto();
    }

    @Override
    public ItemDto updateItemData(String stuffName, int itemNum, ItemDto itemDto) throws NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        ItemId itemId = new ItemId(stuffEntity.getId(), itemNum);

        ItemEntity target = itemRepository.findById(itemId).orElse(null);
        if(target == null) {
            throw new NotFoundException();
        }
        target.setLastHistoryNum(itemDto.getLastHistoryNum());

        ItemEntity savedItemEntity = itemRepository.save(target);
        itemRepository.refresh(savedItemEntity);
        stuffRepository.refresh(stuffEntity);
        return savedItemEntity.toItemDto();
    }
}
