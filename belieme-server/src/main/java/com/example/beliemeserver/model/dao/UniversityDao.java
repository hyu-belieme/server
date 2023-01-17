package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllUniversitiesData() throws DataException;

    @Transactional
    UniversityDto getUniversityByCodeData(String code)
            throws DataException, NotFoundException;

    @Transactional
    UniversityDto addUniversityData(UniversityDto newUniversity)
            throws DataException, ConflictException;

    @Transactional
    UniversityDto updateUniversityData(String code, UniversityDto newUniversityDto)
            throws DataException, NotFoundException, ConflictException;
}
