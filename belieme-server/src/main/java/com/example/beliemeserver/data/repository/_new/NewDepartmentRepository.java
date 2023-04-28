package com.example.beliemeserver.data.repository._new;

import com.example.beliemeserver.data.entity._new.NewDepartmentEntity;
import com.example.beliemeserver.data.repository.custom.RefreshRepository;

import java.util.Optional;
import java.util.UUID;

public interface NewDepartmentRepository extends RefreshRepository<NewDepartmentEntity, UUID> {
    Iterable<NewDepartmentEntity> findByUniversityId(UUID universityId);

    Optional<NewDepartmentEntity> findByUniversityIdAndName(UUID universityId, String name);

    boolean existsByUniversityIdAndName(UUID universityId, String name);
}
