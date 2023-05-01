package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.StuffDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList();

    @Transactional
    List<StuffDto> getListByDepartment(UUID departmentId);

    @Transactional
    List<StuffDto> getListByDepartment(String universityName, String departmentName);

    @Transactional
    StuffDto getById(UUID stuffId);

    @Transactional
    StuffDto getByIndex(String universityName,
                        String departmentName, String stuffName);

    @Transactional
    StuffDto create(StuffDto newStuff);

    @Transactional
    StuffDto update(UUID stuffId, StuffDto newStuff);

    @Transactional
    StuffDto update(String universityName, String departmentName,
                    String stuffName, StuffDto newStuff);
}
