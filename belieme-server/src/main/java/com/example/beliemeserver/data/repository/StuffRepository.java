package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.StuffEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface StuffRepository extends RefreshRepository<StuffEntity, UUID> {
    List<StuffEntity> findByDepartmentId(UUID departmentId);

    Optional<StuffEntity> findByDepartmentIdAndName(UUID departmentId, String name);

    boolean existsByDepartmentIdAndName(UUID departmentId, String name);
}
