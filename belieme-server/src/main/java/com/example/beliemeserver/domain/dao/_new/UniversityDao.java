package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.UniversityDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface UniversityDao {
    @Transactional
    List<UniversityDto> getAllList();

    @Transactional
    UniversityDto getByIndex(String name);

    @Transactional
    UniversityDto getById(UUID id);

    @Transactional
    boolean checkExistById(UUID id);

    @Transactional
    UniversityDto create(UniversityDto newUniversity);

    @Transactional
    UniversityDto update(String name, UniversityDto newUniversityDto);

    @Transactional
    UniversityDto update(UUID id, UniversityDto newUniversityDto);
}
