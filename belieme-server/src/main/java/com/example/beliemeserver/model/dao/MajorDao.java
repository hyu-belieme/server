package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.MajorDto;
import com.example.beliemeserver.exception.ConflictException;
import com.example.beliemeserver.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MajorDao {
    @Transactional
    List<MajorDto> getAllMajorsData();

    @Transactional
    MajorDto getMajorByIndex(String universityCode, String majorCode);

    @Transactional
    MajorDto addMajorData(MajorDto newMajor) throws NotFoundException, ConflictException;

    @Transactional
    MajorDto updateMajorData(String universityCode, String majorCode, MajorDto newMajor) throws NotFoundException, ConflictException;
}
