package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.UniversityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllList();

    @Transactional
    UniversityDto getByIndex(String name);

    @Transactional
    boolean checkExistByIndex(String universityName);

    @Transactional
    UniversityDto create(UniversityDto newUniversity);

    @Transactional
    UniversityDto update(String name, UniversityDto newUniversityDto);
}
