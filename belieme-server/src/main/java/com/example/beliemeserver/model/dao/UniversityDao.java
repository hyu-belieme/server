package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.model.exception.DataException;

import java.util.List;

public interface UniversityDao {
    List<UniversityDto> getAllUniversitiesData() throws DataException;
    UniversityDto getUniversityByCodeData(String code) throws DataException;
    UniversityDto addUniversityData(UniversityDto newUniversity) throws DataException;
    UniversityDto updateUniversityData(String code, UniversityDto newUniversityDto) throws DataException;
}
