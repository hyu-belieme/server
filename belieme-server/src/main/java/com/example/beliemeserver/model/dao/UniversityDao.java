package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UniversityDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllUniversitiesData();

    @Transactional
    UniversityDto getUniversityByCodeData(String code)
            throws NotFoundException;

    @Transactional
    UniversityDto addUniversityData(UniversityDto newUniversity)
            throws ConflictException;

    @Transactional
    UniversityDto updateUniversityData(String code, UniversityDto newUniversityDto)
            throws NotFoundException, ConflictException;
}
