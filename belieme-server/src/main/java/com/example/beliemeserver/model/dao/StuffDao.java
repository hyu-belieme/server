package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.StuffDto;
import com.example.beliemeserver.model.exception.*;

import java.util.List;

public interface StuffDao {
    public List<StuffDto> getStuffsData() throws DataException;
    StuffDto getStuffByNameData(String name) throws NotFoundException, DataException;
    public StuffDto addStuffData(StuffDto newStuff) throws  ConflictException, DataException;
    public StuffDto updateStuffData(String name, StuffDto newStuff) throws NotFoundException, DataException;
}
