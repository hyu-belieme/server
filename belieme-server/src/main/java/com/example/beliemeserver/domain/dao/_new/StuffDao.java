package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.StuffDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList();

    @Transactional
    List<StuffDto> getListByDepartment(String universityName, String departmentName);

    @Transactional
    StuffDto getByIndex(String universityName,
                        String departmentName, String stuffName);

    @Transactional
    StuffDto create(StuffDto newStuff);

    @Transactional
    StuffDto update(String universityName, String departmentName,
                    String stuffName, StuffDto newStuff);
}
