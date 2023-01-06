package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.OldItemEntity;
import com.example.beliemeserver.data.entity.old.OldStuffEntity;
import com.example.beliemeserver.data.entity.id.OldItemId;
import com.example.beliemeserver.data.repository.old.OldItemRepository;
import com.example.beliemeserver.data.repository.old.OldStuffRepository;
import com.example.beliemeserver.model.dao.old.ItemDao;
import com.example.beliemeserver.model.dto.old.OldItemDto;
import com.example.beliemeserver.model.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class OldItemDaoImpl implements ItemDao {
    @Autowired
    OldStuffRepository stuffRepository;

    @Autowired
    OldItemRepository itemRepository;

    @Override
    public List<OldItemDto> getItemsByStuffNameData(String stuffName) throws DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            return new ArrayList<>();
        }

        List<OldItemDto> itemDtoList = new ArrayList<>();
        Iterator<OldItemEntity> iterator = itemRepository.findByStuffId(stuffEntity.getId()).iterator();
        while(iterator.hasNext()) {
            itemDtoList.add(iterator.next().toItemDto());
        }
        return itemDtoList;
    }

    @Override
    public OldItemDto getItemByStuffNameAndItemNumData(String stuffName, int itemNum) throws NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        OldItemId itemId = new OldItemId(stuffEntity.getId(), itemNum);

        OldItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
        if(itemEntity == null) {
            throw new NotFoundException();
        }
        return itemEntity.toItemDto();
    }

    @Override
    public OldItemDto addItemData(OldItemDto newItem) throws ConflictException, NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(newItem.getStuff().getName()).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        int newItemNum = stuffEntity.getAndIncrementNextItemNum();
        OldItemId itemId = new OldItemId(stuffEntity.getId(), newItemNum);
        if(itemRepository.existsById(itemId)) {
            throw new ConflictException();
        }

        OldItemEntity newItemEntity = OldItemEntity.builder()
                .stuffId(stuffEntity.getId())
                .num(newItemNum)
                .lastHistoryNum(null)
                .nextHistoryNum(1)
                .build();

        OldItemEntity savedItemEntity = itemRepository.save(newItemEntity);
        stuffRepository.save(stuffEntity);
        itemRepository.refresh(savedItemEntity);
        stuffRepository.refresh(stuffEntity);
        return savedItemEntity.toItemDto();
    }

    @Override
    public OldItemDto updateItemData(String stuffName, int itemNum, OldItemDto itemDto) throws NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        OldItemId itemId = new OldItemId(stuffEntity.getId(), itemNum);

        OldItemEntity target = itemRepository.findById(itemId).orElse(null);
        if(target == null) {
            throw new NotFoundException();
        }
        target.setLastHistoryNum(itemDto.getLastHistoryNum());

        OldItemEntity savedItemEntity = itemRepository.save(target);
        itemRepository.refresh(savedItemEntity);
        stuffRepository.refresh(stuffEntity);
        return savedItemEntity.toItemDto();
    }
}
