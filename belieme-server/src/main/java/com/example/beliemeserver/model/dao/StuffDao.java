package com.example.beliemeserver.model.dao;

import com.example.beliemeserver.model.dto.StuffDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList();

    @Transactional
    List<StuffDto> getListByDepartment(String universityCode, String departmentCode);

    @Transactional
    StuffDto getByIndex(String universityCode,
                        String departmentCode, String stuffName);

    @Transactional
    StuffDto create(StuffDto newStuff);

    @Transactional
    StuffDto update(String universityCode, String departmentCode,
                    String stuffName, StuffDto newStuff);
}
