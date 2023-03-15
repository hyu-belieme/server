package com.example.beliemeserver.domain.dao;

import com.example.beliemeserver.domain.dto.MajorDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllList();

    @Transactional
    MajorDto getByIndex(String universityCode, String majorCode);

    @Transactional
    MajorDto create(MajorDto newMajor);

    @Transactional
    MajorDto update(String universityCode, String majorCode, MajorDto newMajor);
}
