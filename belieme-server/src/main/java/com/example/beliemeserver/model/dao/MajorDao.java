package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface MajorDao {
    List<MajorDto> getAllMajorsData() throws DataException;
    MajorDto addMajorData(MajorDto newMajor) throws DataException, NotFoundException, ConflictException;
    MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException, ConflictException;
}
