package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import com.example.beliemeserver.model.dto.UniversityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllList();

    @Transactional
    UniversityDto getByIndex(String code)
            throws NotFoundException;

    @Transactional
    boolean checkExistByIndex(String universityCode);

    @Transactional
    UniversityDto create(UniversityDto newUniversity)
            throws ConflictException;

    @Transactional
    UniversityDto update(String code, UniversityDto newUniversityDto)
            throws NotFoundException, ConflictException;
}
