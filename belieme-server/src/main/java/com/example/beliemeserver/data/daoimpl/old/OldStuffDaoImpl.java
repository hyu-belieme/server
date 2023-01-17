package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.OldStuffEntity;
import com.example.beliemeserver.data.repository.old.OldStuffRepository;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.old.StuffDao;
import com.example.beliemeserver.model.dto.old.OldStuffDto;

import com.example.beliemeserver.model.exception.old.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class OldStuffDaoImpl implements StuffDao {
    @Autowired
    OldStuffRepository stuffRepository;

    @Override
    public List<OldStuffDto> getStuffsData() throws DataException {
        Iterator<OldStuffEntity> iterator = stuffRepository.findAll().iterator();
        List<OldStuffDto> stuffDtoList = new ArrayList<>();
        while(iterator.hasNext()) {
            OldStuffEntity tmp = iterator.next();
//            tmp.setNextItemNum(tmp.getItems().size()+1);
            stuffDtoList.add(tmp.toStuffDto());
//            stuffRepository.save(tmp);
        }
        return stuffDtoList;
    }

    @Override
    public OldStuffDto getStuffByNameData(String name) throws NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(name).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }
        return stuffEntity.toStuffDto();
    }

    @Override
    public OldStuffDto addStuffData(OldStuffDto newStuff) throws ConflictException, DataException {
        if(stuffRepository.existsByName(newStuff.getName())) {
            throw new ConflictException();
        }
        OldStuffEntity newStuffEntity = OldStuffEntity.builder()
                .id(OldStuffEntity.getNextId())
                .name(newStuff.getName())
                .emoji(newStuff.getEmoji())
                .nextItemNum(1)
                .build();

        OldStuffEntity savedStuff = stuffRepository.save(newStuffEntity);
        stuffRepository.refresh(savedStuff);

        return savedStuff.toStuffDto();
    }

    @Override
    public OldStuffDto updateStuffData(String name, OldStuffDto newStuff) throws NotFoundException, DataException {
        OldStuffEntity target = stuffRepository.findByName(name).orElse(null);

        if(target == null) {
            throw new NotFoundException();
        }
        target.setName(newStuff.getName());
        target.setEmoji(newStuff.getEmoji());

        OldStuffEntity savedStuff = stuffRepository.save(target);
        stuffRepository.refresh(savedStuff);

        return savedStuff.toStuffDto();
    }
}
