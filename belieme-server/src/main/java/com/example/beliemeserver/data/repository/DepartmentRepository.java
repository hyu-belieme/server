package com.example.beliemeserver.data.repository;

import com.example.beliemeserver.data.entity.DepartmentEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;

public interface DepartmentRepository extends RefreshRepository<DepartmentEntity, Integer> {
    Iterable<DepartmentEntity> findByUniversityId(int universityId);

    Optional<DepartmentEntity> findByUniversityIdAndCode(int universityId, String code);

    boolean existsByUniversityIdAndCode(int universityId, String code);
}
