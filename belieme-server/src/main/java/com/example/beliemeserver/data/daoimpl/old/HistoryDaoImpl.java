package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.HistoryEntity;
import com.example.beliemeserver.data.entity.old.ItemEntity;
import com.example.beliemeserver.data.entity.old.StuffEntity;
import com.example.beliemeserver.data.entity.id.HistoryId;
import com.example.beliemeserver.data.entity.id.ItemId;
import com.example.beliemeserver.data.repository.old.HistoryRepository;
import com.example.beliemeserver.data.repository.old.ItemRepository;
import com.example.beliemeserver.data.repository.old.StuffRepository;
import com.example.beliemeserver.model.dao.old.HistoryDao;
import com.example.beliemeserver.model.dto.old.HistoryDto;
import com.example.beliemeserver.model.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class HistoryDaoImpl implements HistoryDao {
    @Autowired
    StuffRepository stuffRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Override
    public List<HistoryDto> getHistoriesData() throws DataException {
        List<HistoryDto> historyDtoList = new ArrayList<>();

        Iterator<HistoryEntity> iterator = historyRepository.findAll().iterator();
        while(iterator.hasNext()) {
            HistoryEntity tmp = iterator.next();
            historyDtoList.add(tmp.toHistoryDto());
        }
        return historyDtoList;
    }

    @Override
    public List<HistoryDto> getHistoriesByRequesterIdData(String requesterId) throws DataException {
        List<HistoryDto> historyDtoList = new ArrayList<>();

        Iterator<HistoryEntity> iterator = historyRepository.findByRequesterId(requesterId).iterator();
        while(iterator.hasNext()) {
            historyDtoList.add(iterator.next().toHistoryDto());
        }
        return historyDtoList;
    }

    @Override
    public HistoryDto getHistoryByStuffNameAndItemNumAndHistoryNumData(String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        HistoryId historyId = new HistoryId(stuffEntity.getId(), itemNum, historyNum);
        HistoryEntity historyEntity = historyRepository.findById(historyId).orElse(null);
        if(historyEntity == null) {
            throw new NotFoundException();
        }
        return historyEntity.toHistoryDto();
    }

    @Override
    public HistoryDto addHistoryData(HistoryDto newHistory) throws ConflictException, NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(newHistory.getItem().getStuff().getName()).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        ItemId itemId = new ItemId(stuffEntity.getId(), newHistory.getItem().getNum());
        ItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
        if(itemEntity == null) {
            throw new NotFoundException();
        }

        int newHistoryNum = itemEntity.getAndIncrementNextHistoryNum();
        HistoryId historyId = new HistoryId(stuffEntity.getId(), newHistory.getItem().getNum(), newHistoryNum);
        if(historyRepository.existsById(historyId)) {
            throw new ConflictException();
        }

        HistoryEntity newHistoryEntity = HistoryEntity.builder()
                .stuffId(itemEntity.getStuffId())
                .itemNum(itemEntity.getNum())
                .num(newHistoryNum)
                .requesterId(newHistory.getRequesterId())
                .approveManagerId(newHistory.getApproveManagerId())
                .returnManagerId(newHistory.getReturnManagerId())
                .lostManagerId(newHistory.getLostManagerId())
                .cancelManagerId(newHistory.getCancelManagerId())
                .reservedTimeStamp(newHistory.getReservedTimeStamp())
                .approveTimeStamp(newHistory.getApproveTimeStamp())
                .returnTimeStamp(newHistory.getReturnTimeStamp())
                .lostTimeStamp(newHistory.getLostTimeStamp())
                .cancelTimeStamp(newHistory.getCancelTimeStamp())
                .build();

        HistoryEntity savedHistoryEntity = historyRepository.save(newHistoryEntity);
        historyRepository.refresh(savedHistoryEntity);

        itemEntity.setLastHistoryNum(savedHistoryEntity.getNum());
        ItemEntity savedItem = itemRepository.save(itemEntity);
        itemRepository.refresh(savedItem);

        return savedHistoryEntity.toHistoryDto();
    }

    @Override
    public HistoryDto updateHistoryData(String stuffName, int itemNum, int historyNum, HistoryDto newHistory) throws NotFoundException, DataException {
        StuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        HistoryId historyId = new HistoryId(stuffEntity.getId(), itemNum, historyNum);
        HistoryEntity target = historyRepository.findById(historyId).orElse(null);
        if(target == null) {
            throw new NotFoundException();
        }

        target.setRequesterId(newHistory.getRequesterId());
        target.setApproveManagerId(newHistory.getApproveManagerId());
        target.setReturnManagerId(newHistory.getReturnManagerId());
        target.setLostManagerId(newHistory.getLostManagerId());
        target.setCancelManagerId(newHistory.getCancelManagerId());
        target.setReservedTimeStamp(newHistory.getReservedTimeStamp());
        target.setApproveTimeStamp(newHistory.getApproveTimeStamp());
        target.setReturnTimeStamp(newHistory.getReturnTimeStamp());
        target.setLostTimeStamp(newHistory.getLostTimeStamp());
        target.setCancelTimeStamp(newHistory.getCancelTimeStamp());

        HistoryEntity savedHistoryEntity = historyRepository.save(target);
        historyRepository.refresh(savedHistoryEntity);
        return savedHistoryEntity.toHistoryDto();
    }
}
