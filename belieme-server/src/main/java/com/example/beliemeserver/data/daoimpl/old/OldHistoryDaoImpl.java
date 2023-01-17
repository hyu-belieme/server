package com.example.beliemeserver.data.daoimpl.old;

import com.example.beliemeserver.data.entity.old.OldHistoryEntity;
import com.example.beliemeserver.data.entity.old.OldItemEntity;
import com.example.beliemeserver.data.entity.old.OldStuffEntity;
import com.example.beliemeserver.data.entity.id.OldHistoryId;
import com.example.beliemeserver.data.entity.id.OldItemId;
import com.example.beliemeserver.data.repository.old.OldHistoryRepository;
import com.example.beliemeserver.data.repository.old.OldItemRepository;
import com.example.beliemeserver.data.repository.old.OldStuffRepository;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dao.old.HistoryDao;
import com.example.beliemeserver.model.dto.old.OldHistoryDto;

import com.example.beliemeserver.model.exception.old.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class OldHistoryDaoImpl implements HistoryDao {
    @Autowired
    OldStuffRepository stuffRepository;

    @Autowired
    OldItemRepository itemRepository;

    @Autowired
    OldHistoryRepository historyRepository;

    @Override
    public List<OldHistoryDto> getHistoriesData() throws DataException {
        List<OldHistoryDto> historyDtoList = new ArrayList<>();

        Iterator<OldHistoryEntity> iterator = historyRepository.findAll().iterator();
        while(iterator.hasNext()) {
            OldHistoryEntity tmp = iterator.next();
            historyDtoList.add(tmp.toHistoryDto());
        }
        return historyDtoList;
    }

    @Override
    public List<OldHistoryDto> getHistoriesByRequesterIdData(String requesterId) throws DataException {
        List<OldHistoryDto> historyDtoList = new ArrayList<>();

        Iterator<OldHistoryEntity> iterator = historyRepository.findByRequesterId(requesterId).iterator();
        while(iterator.hasNext()) {
            historyDtoList.add(iterator.next().toHistoryDto());
        }
        return historyDtoList;
    }

    @Override
    public OldHistoryDto getHistoryByStuffNameAndItemNumAndHistoryNumData(String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        OldHistoryId historyId = new OldHistoryId(stuffEntity.getId(), itemNum, historyNum);
        OldHistoryEntity historyEntity = historyRepository.findById(historyId).orElse(null);
        if(historyEntity == null) {
            throw new NotFoundException();
        }
        return historyEntity.toHistoryDto();
    }

    @Override
    public OldHistoryDto addHistoryData(OldHistoryDto newHistory) throws ConflictException, NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(newHistory.getItem().getStuff().getName()).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        OldItemId itemId = new OldItemId(stuffEntity.getId(), newHistory.getItem().getNum());
        OldItemEntity itemEntity = itemRepository.findById(itemId).orElse(null);
        if(itemEntity == null) {
            throw new NotFoundException();
        }

        int newHistoryNum = itemEntity.getAndIncrementNextHistoryNum();
        OldHistoryId historyId = new OldHistoryId(stuffEntity.getId(), newHistory.getItem().getNum(), newHistoryNum);
        if(historyRepository.existsById(historyId)) {
            throw new ConflictException();
        }

        OldHistoryEntity newHistoryEntity = OldHistoryEntity.builder()
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

        OldHistoryEntity savedHistoryEntity = historyRepository.save(newHistoryEntity);
        historyRepository.refresh(savedHistoryEntity);

        itemEntity.setLastHistoryNum(savedHistoryEntity.getNum());
        OldItemEntity savedItem = itemRepository.save(itemEntity);
        itemRepository.refresh(savedItem);

        return savedHistoryEntity.toHistoryDto();
    }

    @Override
    public OldHistoryDto updateHistoryData(String stuffName, int itemNum, int historyNum, OldHistoryDto newHistory) throws NotFoundException, DataException {
        OldStuffEntity stuffEntity = stuffRepository.findByName(stuffName).orElse(null);
        if(stuffEntity == null) {
            throw new NotFoundException();
        }

        OldHistoryId historyId = new OldHistoryId(stuffEntity.getId(), itemNum, historyNum);
        OldHistoryEntity target = historyRepository.findById(historyId).orElse(null);
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

        OldHistoryEntity savedHistoryEntity = historyRepository.save(target);
        historyRepository.refresh(savedHistoryEntity);
        return savedHistoryEntity.toHistoryDto();
    }
}
