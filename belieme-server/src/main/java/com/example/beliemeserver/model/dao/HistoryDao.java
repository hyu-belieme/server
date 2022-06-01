package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.HistoryDto;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface HistoryDao {
    public List<HistoryDto> getHistoriesData() throws DataException;
    public List<HistoryDto> getHistoriesByRequesterIdData(String requesterId) throws DataException;
    HistoryDto getHistoryByStuffNameAndItemNumAndHistoryNumData(String stuffName, int itemNum, int historyNum) throws NotFoundException, DataException;
    public HistoryDto addHistoryData(HistoryDto newItem) throws ConflictException, NotFoundException, DataException;
    public HistoryDto updateHistoryData(String stuffName, int itemNum, int historyNum, HistoryDto itemDto) throws NotFoundException, DataException;
}
