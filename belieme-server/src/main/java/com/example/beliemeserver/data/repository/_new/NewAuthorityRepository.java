package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewAuthorityEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewAuthorityRepository extends RefreshRepository<NewAuthorityEntity, Integer> {
    Optional<NewAuthorityEntity> findByDepartmentIdAndPermission(UUID departmentId, String permission);

    boolean existsByDepartmentIdAndPermission(UUID departmentId, String permission);
}
