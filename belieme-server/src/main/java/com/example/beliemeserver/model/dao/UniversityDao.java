package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.ConflictException;
import com.example.beliemeserver.model.exception.DataException;
import com.example.beliemeserver.model.exception.NotFoundException;

import java.util.List;

public interface UniversityDao {
    List<UniversityDto> getAllUniversitiesData() throws DataException;
    UniversityDto getUniversityByCodeData(String code) throws DataException, NotFoundException;
    UniversityDto addUniversityData(UniversityDto newUniversity) throws DataException, ConflictException;
    UniversityDto updateUniversityData(String code, UniversityDto newUniversityDto) throws DataException, NotFoundException, ConflictException;
}
