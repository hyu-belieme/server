package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.DataException;

public interface MajorDao {
    MajorDto addMajorData(MajorDto newMajor) throws DataException;
}
