package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.OldHistoryDto;
import com.example.beliemeserver.model.exception.old.DataException;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;

import java.util.List;

public interface HistoryDao {
    public List<OldHistoryDto> getHistoriesData() throws DataException;
    public List<OldHistoryDto> getHistoriesByRequesterIdData(String requesterId) throws DataException;
    OldHistoryDto getHistoryByStuffNameAndItemNumAndHistoryNumData(String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException;
    public OldHistoryDto addHistoryData(OldHistoryDto newItem) throws ConflictException, NotFoundException, DataException;
    public OldHistoryDto updateHistoryData(String stuffName, int itemNum, int historyNum, OldHistoryDto itemDto) throws NotFoundException, DataException;
}
