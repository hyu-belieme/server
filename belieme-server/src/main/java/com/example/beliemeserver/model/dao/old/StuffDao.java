package com.example.beliemeserver.model.dao.old;

import com.example.beliemeserver.model.dto.old.OldStuffDto;
import com.example.beliemeserver.model.exception.*;

import java.util.List;

public interface StuffDao {
    public List<OldStuffDto> getStuffsData() throws DataException;
    OldStuffDto getStuffByNameData(String name) throws NotFoundException, DataException;
    public OldStuffDto addStuffData(OldStuffDto newStuff) throws  ConflictException, DataException;
    public OldStuffDto updateStuffData(String name, OldStuffDto newStuff) throws NotFoundException, DataException;
}
