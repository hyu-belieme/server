package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends RefreshRepository<DepartmentEntity, UUID> {
    Iterable<DepartmentEntity> findByUniversityId(UUID universityId);

    Optional<DepartmentEntity> findByUniversityIdAndName(UUID universityId, String name);

    boolean existsByUniversityIdAndName(UUID universityId, String name);
}
