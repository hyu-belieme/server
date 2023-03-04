package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.UniversityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllList();

    @Transactional
    UniversityDto getByIndex(String code);

    @Transactional
    boolean checkExistByIndex(String universityCode);

    @Transactional
    UniversityDto create(UniversityDto newUniversity);

    @Transactional
    UniversityDto update(String code, UniversityDto newUniversityDto);
}
