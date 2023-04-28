package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.MajorDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllList();

    @Transactional
    MajorDto getByIndex(String universityName, String majorCode);

    @Transactional
    MajorDto create(MajorDto newMajor);

    @Transactional
    MajorDto update(String universityName, String majorCode, MajorDto newMajor);
}
