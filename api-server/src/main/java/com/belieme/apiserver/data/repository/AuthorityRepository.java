package com.belieme.apiserver.data.repository;

import com.belieme.apiserver.data.entity.AuthorityEntity;
import com.belieme.apiserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends RefreshRepository<AuthorityEntity, Integer> {
    Optional<AuthorityEntity> findByDepartmentIdAndPermission(UUID departmentId, String permission);

    boolean existsByDepartmentIdAndPermission(UUID departmentId, String permission);
}
