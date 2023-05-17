package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.DepartmentEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends RefreshRepository<DepartmentEntity, UUID> {
    Iterable<DepartmentEntity> findByUniversityId(UUID universityId);

    Optional<DepartmentEntity> findByUniversityIdAndName(UUID universityId, String name);

    boolean existsByUniversityIdAndName(UUID universityId, String name);
}
