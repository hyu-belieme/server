package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.StuffDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface StuffDao {
    @Transactional
    List<StuffDto> getAllList();

    @Transactional
    List<StuffDto> getListByDepartment(@NonNull UUID departmentId);

    @Transactional
    StuffDto getById(@NonNull UUID stuffId);

    @Transactional
    StuffDto create(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name, String thumbnail);

    @Transactional
    StuffDto update(@NonNull UUID stuffId, @NonNull UUID departmentId, @NonNull String name, String thumbnail);
}
