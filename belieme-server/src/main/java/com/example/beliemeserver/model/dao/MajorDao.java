package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllMajorsData() throws DataException;

    @Transactional
    MajorDto addMajorData(MajorDto newMajor) throws DataException, NotFoundException, ConflictException;

    @Transactional
    MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws DataException, NotFoundException, ConflictException;
}
