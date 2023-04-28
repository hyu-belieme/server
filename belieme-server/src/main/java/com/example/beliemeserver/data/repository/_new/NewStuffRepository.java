package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewStuffEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NewStuffRepository extends RefreshRepository<NewStuffEntity, UUID> {
    List<NewStuffEntity> findByDepartmentId(UUID departmentId);

    Optional<NewStuffEntity> findByDepartmentIdAndName(UUID departmentId, String name);

    boolean existsByDepartmentIdAndName(UUID departmentId, String name);
}
