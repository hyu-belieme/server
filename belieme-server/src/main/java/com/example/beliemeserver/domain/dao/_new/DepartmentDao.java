package com.example.beliemeserver.domain.dao._new;

import com.example.beliemeserver.domain.dto._new.DepartmentDto;
import com.example.beliemeserver.domain.dto._new.MajorDto;
import com.example.beliemeserver.domain.dto._new.UniversityDto;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface DepartmentDao {
    @Transactional
    List<DepartmentDto> getAllList();

    @Transactional
    List<DepartmentDto> getListByUniversity(@NonNull UUID universityId);

    @Transactional
    DepartmentDto getById(@NonNull UUID departmentId);

    @Transactional
    boolean checkExistById(@NonNull UUID id);

    @Transactional
    DepartmentDto create(@NonNull UUID departmentId, @NonNull UUID universityId, @NonNull String name, @NonNull List<UUID> majorId);

    @Transactional
    DepartmentDto update(@NonNull UUID departmentId, @NonNull UUID universityId, @NonNull String name, @NonNull List<UUID> majorId);
}
