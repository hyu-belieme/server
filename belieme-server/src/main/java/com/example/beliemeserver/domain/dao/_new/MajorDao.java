package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.MajorDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllList();

    @Transactional
    MajorDto getById(UUID majorId);

    @Transactional
    MajorDto getByIndex(UUID universityId, String majorCode);

    @Transactional
    MajorDto create(MajorDto newMajor);

    @Transactional
    MajorDto update(UUID majorId, MajorDto newMajor);
}
