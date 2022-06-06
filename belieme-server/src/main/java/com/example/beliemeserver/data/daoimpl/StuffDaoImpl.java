package com.example.beliemeserver.data.daoimpl;

import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.StuffRepository;
import com.example.beliemeserver.model.dao.StuffDao;
import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class StuffDaoImpl implements StuffDao {
    @Autowired
    StuffRepository stuffRepository;

    @Override
    public List<StuffDto> getStuffsData() throws DataException {
        Iterator<StuffEntity> iterator = stuffRepository.findAll().iterator();
        List<StuffDto> stuffDtoList = new ArrayList<>();
        while(iterator.hasNext()) {
            StuffEntity tmp = iterator.next();
//            tmp.setNextItemNum(tmp.getItems().size()+1);
            stuffDtoList.add(tmp.toStuffDto());
//            stuffRepository.save(tmp);
        }
        return stuffDtoList;
    }

    @Override
    public StuffDto getStuffByNameData(String name) throws NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(name).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }
        return stuffEntity.toStuffDto();
    }

    @Override
    public StuffDto addStuffData(StuffDto newStuff) throws ConflictException, DataException {
        if(stuffRepository.existsByName(newStuff.getName())) {
            throw new ConflictException();
        }
        StuffEntity newStuffEntity = StuffEntity.builder()
                .id(StuffEntity.getNextId())
                .name(newStuff.getName())
                .emoji(newStuff.getEmoji())
                .nextItemNum(1)
                .build();

        StuffEntity savedStuff = stuffRepository.save(newStuffEntity);
        stuffRepository.refresh(savedStuff);

        return savedStuff.toStuffDto();
    }

    @Override
    public StuffDto updateStuffData(String name, StuffDto newStuff) throws NotFoundException, DataException {
        StuffEntity target = stuffRepository.findByName(name).orElse(null);

        if(target == null) {
            throw new NotFoundException();
        }
        target.setName(newStuff.getName());
        target.setEmoji(newStuff.getEmoji());

        StuffEntity savedStuff = stuffRepository.save(target);
        stuffRepository.refresh(savedStuff);

        return savedStuff.toStuffDto();
    }
}
